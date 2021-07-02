package com.duckvis.nuguri.repository

import com.duckvis.core.domain.nuguri.AttendanceCard
import com.duckvis.core.domain.nuguri.AttendanceCardRepository
import com.duckvis.core.dtos.nuguri.Work
import com.duckvis.core.utils.DateTimeMaker
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class AttendanceCardNuguriRepositoryTest(
  @Autowired private val attendanceCardNuguriRepository: AttendanceCardNuguriRepository,
  @Autowired private val attendanceCardRepository: AttendanceCardRepository,
) {

  @AfterEach
  fun clear() {
    attendanceCardRepository.deleteAllInBatch()
  }

  @Test
  fun getAttendedAt() {
    // given
    attendanceCardRepository.save(
      AttendanceCard(
        Work(1L, 1L, null),
        loginDateTime = DateTimeMaker.nowDateTime().withHour(4).withMinute(30)
      )
    )
    attendanceCardRepository.save(
      AttendanceCard(
        Work(2L, 1L, null),
        loginDateTime = DateTimeMaker.nowDateTime().withHour(3).withMinute(32)
      )
    )

    // when
    val cards = attendanceCardNuguriRepository.getAttendedAt(DateTimeMaker.nowDateTime().withHour(4).withMinute(1))

    // then
    assertThat(cards).hasSize(1)
    assertThat(cards[0].userId).isEqualTo(2)
  }

  @Test
  fun getCoreTimeCards() {
    // given
    attendanceCardRepository.save(
      AttendanceCard(
        Work(1L, 1L, null),
        loginDateTime = DateTimeMaker.nowDateTime().withHour(4).withMinute(30)
      )
    )
    attendanceCardRepository.save(
      AttendanceCard(
        Work(2L, 1L, null),
        loginDateTime = DateTimeMaker.nowDateTime().withHour(3).withMinute(32)
      )
    )

    // when
    val cards = attendanceCardNuguriRepository.getCoreTimeCards(DateTimeMaker.nowDate())

    // then
    assertThat(cards).hasSize(2)
    assertThat(cards.map { it.userId }).containsOnly(1, 2)
  }

}