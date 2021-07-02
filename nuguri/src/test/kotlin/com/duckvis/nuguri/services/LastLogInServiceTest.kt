package com.duckvis.nuguri.services

import com.duckvis.core.domain.nuguri.AttendanceCard
import com.duckvis.core.domain.nuguri.AttendanceCardRepository
import com.duckvis.core.domain.nuguri.Project
import com.duckvis.core.domain.nuguri.ProjectRepository
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.Work
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.nuguri.domain.attendance.service.LastLogInService
import com.duckvis.nuguri.utils.AssertNuguriException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.Duration
import java.time.LocalDateTime
import javax.transaction.Transactional

@SpringBootTest
@ActiveProfiles("test")
class LastLogInServiceTest(
  @Autowired private val lastLogInService: LastLogInService,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val attendanceCardRepository: AttendanceCardRepository,
  @Autowired private val projectRepository: ProjectRepository,
) {
  @BeforeEach
  @Transactional
  fun prepare() {
    userRepository.save(User("user", "user"))
    projectRepository.save(Project("기본", "ㄱㅂ"))
  }

  @AfterEach
  @Transactional
  fun clear() {
    attendanceCardRepository.deleteAllInBatch()
    projectRepository.deleteAllInBatch()
    userRepository.deleteAllInBatch()
  }

  @Transactional
  fun attendBeforeHours(userName: String, hours: Long, projectName: String = "기본"): AttendanceCard {
    val user = userRepository.findByName(userName) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val project = projectRepository.findByNameOrNickname(projectName, projectName) ?: throw NuguriException(
      ExceptionType.NO_SUCH_PROJECT
    )
    val work = Work(user.id, project.id, null)
    return attendanceCardRepository.save(AttendanceCard(work, DateTimeMaker.nowDateTime().minusHours(hours)))
  }

  @Transactional
  fun makeLogOutCard(
    userName: String,
    hours: Long,
    projectName: String = "기본",
    logoutDateTime: LocalDateTime = DateTimeMaker.nowDateTime().minusHours(2)
  ): AttendanceCard {
    val user = userRepository.findByName(userName) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val project = projectRepository.findByNameOrNickname(projectName, projectName) ?: throw NuguriException(
      ExceptionType.NO_SUCH_PROJECT
    )
    val work = Work(user.id, project.id, null)
    return attendanceCardRepository.save(
      AttendanceCard(
        work,
        logoutDateTime.minusHours(hours),
        (hours * 3600).toInt(),
        logoutDateTime
      )
    )
  }

  @Test
  fun `마지막 출근을 물어본다`() {
    // given
    attendBeforeHours("user", 14)

    // when
    val card = lastLogInService.lastLogIn("user")

    // then
    println(card.loginDateTime)
    assertThat(card.loginDateTime).isBeforeOrEqualTo(DateTimeMaker.nowDateTime().minusHours(14))
    assertThat(card.durationSeconds).isNull()
    assertThat(card.logoutDateTime).isNull()
    assertThat(card.isNowWorking).isTrue
  }

  @Test
  fun `출근 한 번도 안 했는데 마지막 출근을 입력한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.NEVER_ATTENDED).assert {
      lastLogInService.lastLogIn("user")
    }
  }

}