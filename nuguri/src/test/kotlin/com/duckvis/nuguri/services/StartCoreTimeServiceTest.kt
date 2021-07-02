package com.duckvis.nuguri.services

import com.duckvis.core.domain.nuguri.AttendanceCard
import com.duckvis.core.domain.nuguri.AttendanceCardRepository
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.Work
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.nuguri.domain.admin.service.StartCoreTimeService
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
class StartCoreTimeServiceTest(
  @Autowired private val startCoreTimeService: StartCoreTimeService,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val attendanceCardRepository: AttendanceCardRepository,
) {
  @BeforeEach
  @Transactional
  fun prepare() {
    userRepository.save(User("user1", "user1"))
    userRepository.save(User("user2", "user2"))
    userRepository.save(User("user3", "user3"))
  }

  @AfterEach
  @Transactional
  fun clear() {
    attendanceCardRepository.deleteAllInBatch()
    userRepository.deleteAllInBatch()
  }

  @Transactional
  fun attendHoursBefore(userName: String, hours: Long, projectId: Long): AttendanceCard {
    val user = userRepository.findByName(userName) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    return attendanceCardRepository.save(
      AttendanceCard(
        Work(user.id, projectId, null),
        DateTimeMaker.nowDateTime().minusHours(hours)
      )
    )
  }

  @Transactional
  fun exit(userName: String): User {
    val user = userRepository.findByName(userName) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    user.exit()
    userRepository.save(user)

    return user
  }

  @Test
  fun `2명 출근해 있을 때 코어타임이 시작된다`() {
    // given
    attendHoursBefore("user2", 2, 1)
    attendHoursBefore("user3", 3, 2)

    // when
    val notLoggedIn = startCoreTimeService.getNotLoggedIn()

    // then
    assertThat(notLoggedIn).hasSize(1)
    assertThat(notLoggedIn[0].name).isEqualTo("user1")
  }

  @Test
  fun `모두다 출근해 있을 때 코어타임이 시작된다`() {
    // given
    attendHoursBefore("user1", 2, 1)
    attendHoursBefore("user2", 3, 2)
    attendHoursBefore("user3", 1, 1)

    // when
    val notLoggedIn = startCoreTimeService.getNotLoggedIn()

    // then
    assertThat(notLoggedIn).isEmpty()
  }

  @Test
  fun `아무도 출근해 있지 않을 때 코어타임이 시작된다`() {
    // given

    // when
    val notLoggedIn = startCoreTimeService.getNotLoggedIn()

    // then
    assertThat(notLoggedIn).hasSize(3)
    AssertNuguriException(ExceptionType.NOBODY_IS_WORKING).assert {
      startCoreTimeService.isEverybodyAbsent(notLoggedIn)
    }
  }

  @Test
  fun `3명 중 한 명은 퇴사, 1명만 출근했을 때 코어타임이 시작된다`() {
    // given
    val user = exit("user1")
    attendHoursBefore("user2", 3, 1)

    // when
    val notLoggedIn = startCoreTimeService.getNotLoggedIn()

    // then
    assertThat(notLoggedIn).hasSize(1)
    assertThat(notLoggedIn[0].name).isEqualTo("user3")
  }
}