package com.catshi.bob.services

import com.catshi.bob.domain.BobTicket

interface TeamSortStrategy {

    fun sort(originalList: List<BobTicket>): List<BobTicket>

}

object RandomTeamSortStrategy : TeamSortStrategy {

    override fun sort(originalList: List<BobTicket>): List<BobTicket> {
        return originalList.shuffled()
    }

}

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