package com.duckvis.bob.services

import com.duckvis.core.domain.bob.BobTicket

interface TeamSortStrategy {

  fun sort(originalList: List<BobTicket>): List<BobTicket>

}

// dev, prod
object RandomTeamSortStrategy : TeamSortStrategy {

  override fun sort(originalList: List<BobTicket>): List<BobTicket> {
    return originalList.shuffled()
  }

}

// local
object FifoTeamSortStrategy : TeamSortStrategy {

  override fun sort(originalList: List<BobTicket>): List<BobTicket> {
    return originalList
  }

}

object TicketTeamSortStrategy : TeamSortStrategy {

  override fun sort(originalList: List<BobTicket>): List<BobTicket> {
    return originalList.sortedBy { it.date }
  }

}