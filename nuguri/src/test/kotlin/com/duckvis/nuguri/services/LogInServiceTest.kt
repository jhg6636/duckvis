package com.duckvis.nuguri.services

import com.duckvis.core.domain.nuguri.*
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.Work
import com.duckvis.core.dtos.nuguri.WorkTypeDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.nuguri.domain.attendance.service.LogInService
import com.duckvis.nuguri.utils.AssertNuguriException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
class LogInServiceTest(
  @Autowired private val logInService: LogInService,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val projectRepository: ProjectRepository,
  @Autowired private val attendanceCardRepository: AttendanceCardRepository,
  @Autowired private val subProjectRepository: SubProjectRepository,
) {
  @BeforeEach
  @Transactional
  fun prepare() {
    userRepository.save(User("user", "user"))
    val p1 = projectRepository.save(Project("기본1", "ㄱㅂ1"))
    projectRepository.save(Project("기본2", "ㄱㅂ2"))
    subProjectRepository.save(SubProject(p1.id, "섭1", "ㅅ1"))
    subProjectRepository.save(SubProject(p1.id, "섭2", "ㅅ2"))
  }

  @AfterEach
  @Transactional
  fun clear() {
    attendanceCardRepository.deleteAllInBatch()
    projectRepository.deleteAllInBatch()
    userRepository.deleteAllInBatch()
  }

  @Transactional
  fun attendBeforeHours(
    userName: String,
    hours: Long,
    projectName: String = "기본1",
    subProjectName: String? = null
  ): AttendanceCard {
    val user = userRepository.findByName(userName) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val project = projectRepository.findByNameOrNickname(projectName, projectName) ?: throw NuguriException(
      ExceptionType.NO_SUCH_PROJECT
    )
    val subProject = subProjectRepository.findByNameAndProjectId(subProjectName ?: "", project.id)
    val work = Work(user.id, project.id, subProject?.id)
    return attendanceCardRepository.save(AttendanceCard(work, DateTimeMaker.nowDateTime().minusHours(hours)))
  }

  @Test
  fun `현재 출근해 있지 않은 상태에서 출근한다`() {
    // given
    val user = userRepository.findByName("user") ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val project =
      projectRepository.findByNameOrNickname("ㄱㅂ1", "ㄱㅂ1") ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)

    // when
    val loginResponse = logInService.logIn(Work(user.id, project.id, null))

    // then
    assertThat(loginResponse.userName).isEqualTo("user")
    assertThat(loginResponse.projectName).isEqualTo(project.fullName)
    assertThat(attendanceCardRepository.count()).isEqualTo(1)
  }

  @Test
  fun `현재 다른 프로젝트에 출근한 상태에서 출근한다`() {
    // given
    val user = userRepository.findByName("user") ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val project =
      projectRepository.findByNameOrNickname("ㄱㅂ2", "ㄱㅂ2") ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)
    attendBeforeHours("user", 2)

    // when
    val loginResponse = logInService.logIn(Work(user.id, project.id, null))
    val beforeCard =
      attendanceCardRepository.findAll().filter { it.loginDateTime < DateTimeMaker.nowDateTime().minusHours(1) }
    println(beforeCard[0].durationSeconds)
    println(beforeCard[0].logoutDateTime)

    // then
    assertThat(loginResponse.userName).isEqualTo("user")
    assertThat(loginResponse.projectName).isEqualTo(project.fullName)
    assertThat(attendanceCardRepository.count()).isEqualTo(2)
    assertThat(beforeCard).hasSize(1)
    assertThat(beforeCard[0].durationSeconds).isEqualTo(7200)
  }

  @Test
  fun `현재 출근해 있는 프로젝트로 출근한다`() {
    // given
    val user = userRepository.findByName("user") ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val project =
      projectRepository.findByNameOrNickname("ㄱㅂ1", "ㄱㅂ1") ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)
    attendBeforeHours("user", 2)

    // when & then
    AssertNuguriException(ExceptionType.ALREADY_ATTENDED).assert {
      logInService.logIn(Work(user.id, project.id, null))
    }
  }

  @Test
  fun `현재 출근해 있는 프로젝트로 다른 옵션으로 출근한다`() {
    // given
    val user = userRepository.findByName("user") ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val project =
      projectRepository.findByNameOrNickname("ㄱㅂ1", "ㄱㅂ1") ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)
    attendBeforeHours("user", 2)

    // when
    val logInResponse = logInService.logIn(
      Work(
        user.id,
        project.id,
        null,
        WorkTypeDto(
          isNight = false,
          isExtended = true,
          isHoliday = false
        )
      )
    )
    val beforeCard = attendanceCardRepository.findAll().filter { !it.isNowWorking }

    // then
    assertThat(logInResponse.userName).isEqualTo("user")
    assertThat(logInResponse.projectName).isEqualTo("기본1(ㄱㅂ1)")
    assertThat(logInResponse.beforeProject).isEqualTo("기본1(ㄱㅂ1)")
    assertThat(attendanceCardRepository.count()).isEqualTo(2)
    assertThat(beforeCard).hasSize(1)
    assertThat(beforeCard[0].durationSeconds).isEqualTo(7200)
  }

  @Test
  fun `서브 플젝을 포함하여 출근한다`() {
    // given
    val user = userRepository.findByName("user") ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val project =
      projectRepository.findByNameOrNickname("ㄱㅂ1", "ㄱㅂ1") ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)
    val subProject = subProjectRepository.findByNameAndProjectId("섭1", project.id)

    // when
    val loginResponse = logInService.logIn(Work(user.id, project.id, subProject?.id))

    // then
    assertThat(loginResponse.userName).isEqualTo("user")
    assertThat(loginResponse.projectName).isEqualTo(project.fullName)
    assertThat(attendanceCardRepository.count()).isEqualTo(1)
  }

  @Test
  fun `섭1 서브플젝에 출근해 있다가 섭2로 바꿔서 출근한다`() {
    // given
    val user = userRepository.findByName("user") ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val project =
      projectRepository.findByNameOrNickname("ㄱㅂ1", "ㄱㅂ1") ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)
    attendBeforeHours("user", 2, "ㄱㅂ1", "섭1")

    val changingSubProject = subProjectRepository.findByNameAndProjectId("섭2", project.id)

    // when
    val loginResponse = logInService.logIn(Work(user.id, project.id, changingSubProject?.id))
    val beforeCard =
      attendanceCardRepository.findAll().filter { it.loginDateTime < DateTimeMaker.nowDateTime().minusHours(1) }

    // then
    assertThat(loginResponse.userName).isEqualTo("user")
    assertThat(loginResponse.projectName).isEqualTo(project.fullName)
    assertThat(attendanceCardRepository.count()).isEqualTo(2)
    assertThat(beforeCard).hasSize(1)
    assertThat(beforeCard[0].durationSeconds).isEqualTo(7200)
    assertThat(beforeCard[0].subProjectId).isNotNull
  }

  @Test
  fun `같은 서브플젝에 출근해 있는데, 다시 출근한다`() {
    // given
    val user = userRepository.findByName("user") ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val project =
      projectRepository.findByNameOrNickname("ㄱㅂ1", "ㄱㅂ1") ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)
    attendBeforeHours("user", 2, "ㄱㅂ1", "섭1")

    val subProject = subProjectRepository.findByNameAndProjectId("섭1", project.id)

    // when & then
    AssertNuguriException(ExceptionType.ALREADY_ATTENDED).assert {
      logInService.logIn(Work(user.id, project.id, subProject?.id))
    }
  }

}