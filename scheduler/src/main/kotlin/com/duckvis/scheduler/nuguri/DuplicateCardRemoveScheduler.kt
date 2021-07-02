package com.duckvis.scheduler.nuguri

import com.duckvis.core.domain.nuguri.AttendanceCardRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.nuguri.repository.AttendanceCardNuguriRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class DuplicateCardRemoveScheduler(
  private val attendanceCardNuguriRepository: AttendanceCardNuguriRepository,
  private val attendanceCardRepository: AttendanceCardRepository,
) {

  @Scheduled(cron="0 * * * * *")
  fun removeDuplicateCards() {
    val workingCards = attendanceCardNuguriRepository.getNowWorkingCards()
    val cardGroup = workingCards.groupBy { card ->
      card.userId
    }
    cardGroup.forEach { (_, cards) ->
      if (cards.size > 1) {
        cards
          .sortedBy { card -> card.loginDateTime }
          .subList(1, cards.size)
          .forEach { card ->
            attendanceCardRepository.delete(card)
          }
      }
    }
  }

}