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
import com.duckvis.nuguri.domain.attendance.service.LogOutService
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
class LogOutServiceTest(
  @Autowired private val logOutService: LogOutService,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val projectRepository: ProjectRepository,
  @Autowired private val attendanceCardRepository: AttendanceCardRepository,
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
  fun attendBeforeHours(user: User, hours: Long): AttendanceCard {
    val project =
      projectRepository.findByNameOrNickname("ㄱㅂ", "ㄱㅂ") ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)
    val work = Work(user.id, project.id, null)
    return attendanceCardRepository.save(
      AttendanceCard(
        work,
        loginDateTime = DateTimeMaker.nowDateTime().minusHours(hours)
      )
    )
  }

  @Test
  fun `출근해 있지 않은데 퇴근한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.NOT_WORKING).assert {
      logOutService.logOut("user")
    }
  }

  @Test
  fun `출근해 있을 때 퇴근한다`() {
    // given
    val user = userRepository.findByName("user") ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    attendBeforeHours(user, 3)

    // when
    val logOutMessage = logOutService.logOut("user")
    val logOutCard = attendanceCardRepository.findAll()[0]

    // then
    assertThat(logOutMessage).isEqualTo("기본(ㄱㅂ) 프로젝트에 대하여 퇴근하셨군요.\n\n3시간 0분 0초 동안 고생 많았어요.")
    assertThat(logOutCard.durationSeconds).isEqualTo(10800)
    assertThat(logOutCard.isNowWorking).isFalse
    assertThat(logOutCard.userId).isEqualTo(user.id)
  }
}