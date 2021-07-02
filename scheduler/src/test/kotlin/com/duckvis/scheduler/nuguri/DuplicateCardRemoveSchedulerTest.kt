package com.duckvis.scheduler.nuguri

import com.duckvis.core.domain.nuguri.AttendanceCard
import com.duckvis.core.domain.nuguri.AttendanceCardRepository
import com.duckvis.core.dtos.nuguri.Work
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import javax.transaction.Transactional

@SpringBootTest
internal class DuplicateCardRemoveSchedulerTest(
  @Autowired private val attendanceCardRepository: AttendanceCardRepository,
  @Autowired private val duplicateCardRemoveScheduler: DuplicateCardRemoveScheduler,
) {

  @AfterEach
  fun clear() {
    attendanceCardRepository.deleteAllInBatch()
  }

  @Test
  fun `잘 제거한다`() {
    // given
    val cards = makeCard()

    // when
    duplicateCardRemoveScheduler.removeDuplicateCards()

    // then
    assertThat(attendanceCardRepository.count()).isEqualTo(1)
    assertThat(attendanceCardRepository.findAll()[0].loginDateTime).isEqualTo(cards[1].loginDateTime)
  }

  @Transactional
  fun makeCard(): List<AttendanceCard> {
    val card1 = attendanceCardRepository.save(AttendanceCard(Work(1L, 1L, null), loginDateTime = LocalDateTime.now()))
    val card2 = attendanceCardRepository.save(AttendanceCard(Work(1L, 1L, null), loginDateTime = LocalDateTime.now().minusMinutes(15)))

    return listOf(card1, card2)
  }

}