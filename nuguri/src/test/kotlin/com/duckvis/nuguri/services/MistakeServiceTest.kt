package com.duckvis.nuguri.services

import com.duckvis.core.domain.nuguri.AttendanceCardRepository
import com.duckvis.core.domain.nuguri.Project
import com.duckvis.core.domain.nuguri.ProjectRepository
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.Mistake
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.CardType
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.nuguri.domain.attendance.service.MistakeService
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
class MistakeServiceTest(
  @Autowired private val mistakeService: MistakeService,
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
    userRepository.deleteAllInBatch()
    projectRepository.deleteAllInBatch()
  }

  @Test
  fun `실수를 입력한다`() {
    // given
    val user = userRepository.findByName("user") ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val project =
      projectRepository.findByNameOrNickname("ㄱㅂ", "ㄱㅂ") ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)

    // when
    val card = mistakeService.saveMistakeCard(Mistake(user.id, project.id, null, 1800), null)

    // then
    assertThat(card.durationSeconds).isEqualTo(1800)
    assertThat(card.isNowWorking).isFalse
    assertThat(card.type).isEqualTo(CardType.MISTAKE)
    assertThat(card.userId).isEqualTo(user.id)
    assertThat(attendanceCardRepository.count()).isEqualTo(1)
  }

  @Test
  fun `어제 날짜로 실수를 입력한다`() {
    // given
    val user = userRepository.findByName("user") ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val project =
      projectRepository.findByNameOrNickname("ㄱㅂ", "ㄱㅂ") ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)

    // when
    val card =
      mistakeService.saveMistakeCard(Mistake(user.id, project.id, null, 1800), DateTimeMaker.nowDate().minusDays(1))
    val todayCards =
      attendanceCardRepository.findAll().filter { it.loginDateTime.toLocalDate() == DateTimeMaker.nowDate() }
    val yesterdayCards =
      attendanceCardRepository.findAll()
        .filter { it.loginDateTime.toLocalDate() == DateTimeMaker.nowDate().minusDays(1) }

    // then
    assertThat(card.durationSeconds).isEqualTo(1800)
    assertThat(card.isNowWorking).isFalse
    assertThat(card.type).isEqualTo(CardType.MISTAKE)
    assertThat(card.userId).isEqualTo(user.id)
    assertThat(todayCards).hasSize(0)
    assertThat(yesterdayCards).hasSize(1)
  }

}