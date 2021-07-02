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
import com.duckvis.nuguri.domain.attendance.service.HowLongIWorkedService
import com.duckvis.nuguri.utils.AssertNuguriException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import javax.transaction.Transactional

@SpringBootTest
@ActiveProfiles("test")
class HowLongIWorkedServiceTest(
  @Autowired private val howLongIWorkedService: HowLongIWorkedService,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val attendanceCardRepository: AttendanceCardRepository,
  @Autowired private val projectRepository: ProjectRepository,
) {
  @BeforeEach
  @Transactional
  fun prepare() {
    userRepository.save(User("user1", "user1"))
    userRepository.save(User("user2", "user2"))
    projectRepository.save(Project("기본", "ㄱㅂ"))
    projectRepository.save(Project("회의", "ㅎㅇ"))
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
    logoutDateTime: LocalDateTime = DateTimeMaker.nowDateTime()
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
  fun `현재 출근중인 상태에서 몇시간을 입력한다`() {
    // given
    attendBeforeHours("user1", 1)

    // when
    val response = howLongIWorkedService.howLongIWorked("user1")

    // then
    assertThat(response.nowWorkSeconds).isEqualTo(3600)
    assertThat(response.todayWorkSeconds).isEqualTo(3600)
  }

  @Test
  fun `오늘 중에 일한 기록이 있고 현재 출근중인 상태에서 몇시간을 입력한다`() {
    // given
    makeLogOutCard("user1", 3)
    attendBeforeHours("user1", 2)

    // when
    val response = howLongIWorkedService.howLongIWorked("user1")

    // then
    assertThat(response.nowWorkSeconds).isEqualTo(7200)
    assertThat(response.todayWorkSeconds).isEqualTo(18000)
  }

  @Test
  fun `출근중이 아닌데, 몇시간을 입력한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.NOT_WORKING).assert {
      howLongIWorkedService.howLongIWorked("user1")
    }
  }
}