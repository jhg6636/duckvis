package com.duckvis.nuguri.services

import com.duckvis.core.domain.nuguri.AttendanceCard
import com.duckvis.core.domain.nuguri.AttendanceCardRepository
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.Work
import com.duckvis.core.dtos.nuguri.WorkTypeDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.utils.StartAndEndTime
import com.duckvis.core.utils.dayStartTime
import com.duckvis.core.utils.weekEndTime
import com.duckvis.core.utils.weekStartTime
import com.duckvis.nuguri.domain.attendance.service.WorkTimeService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
class WorkTimeServiceTest(
  @Autowired private val workTimeService: WorkTimeService,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val attendanceCardRepository: AttendanceCardRepository,
) {
  @BeforeEach
  @Transactional
  fun prepare() {
    userRepository.save(User("user", "user"))
  }

  @AfterEach
  @Transactional
  fun clear() {
    attendanceCardRepository.deleteAllInBatch()
    userRepository.deleteAllInBatch()
  }

  @Transactional
  fun attendBetween(
    userName: String,
    startTime: LocalDateTime,
    endTime: LocalDateTime,
    projectId: Long,
    workTypeDto: WorkTypeDto = WorkTypeDto()
  ): AttendanceCard {
    val user = userRepository.findByName(userName) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val work = Work(user.id, projectId, null, workTypeDto)
    val card = AttendanceCard(work, startTime, Duration.between(startTime, endTime).toSeconds().toInt(), endTime)

    return attendanceCardRepository.save(card)
  }

  @Test
  fun `이번 주에 기본 옵션으로 8시간 일한 사람의 이번 주 workTimeDto를 받는다`() {
    // given
    val user = userRepository.findByName("user") ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    attendBetween(
      "user",
      StartAndEndTime.coreTimeStart.weekStartTime.plusHours(1),
      StartAndEndTime.coreTimeEnd.weekStartTime.plusHours(8),
      1
    )
    attendBetween(
      "user",
      StartAndEndTime.coreTimeStart.dayStartTime,
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(1),
      2
    )

    // when
    val workTime = workTimeService.workTime(
      user.id,
      StartAndEndTime.coreTimeStart.weekStartTime,
      StartAndEndTime.coreTimeEnd.weekEndTime
    )

    // then
    assertThat(workTime.all).isEqualTo(28800)
    assertThat(workTime.night).isEqualTo(0)
    assertThat(workTime.extended).isEqualTo(0)
    assertThat(workTime.holiday).isEqualTo(0)
  }

  @Test
  fun `이번 주에 연장 옵션으로만 5시간 일한 사람의 이번 주 workTimeDto를 받는다`() {
    // given
    val user = userRepository.findByName("user") ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    attendBetween(
      "user",
      StartAndEndTime.coreTimeStart.weekStartTime.plusHours(22),
      StartAndEndTime.coreTimeEnd.weekStartTime.plusHours(27),
      1,
      WorkTypeDto(isExtended = true)
    )

    // when
    val workTime = workTimeService.workTime(
      user.id,
      StartAndEndTime.coreTimeStart.weekStartTime,
      StartAndEndTime.coreTimeEnd.weekEndTime
    )

    // then
    assertThat(workTime.all).isEqualTo(18000)
    assertThat(workTime.night).isEqualTo(0)
    assertThat(workTime.extended).isEqualTo(18000)
    assertThat(workTime.holiday).isEqualTo(0)
  }
}