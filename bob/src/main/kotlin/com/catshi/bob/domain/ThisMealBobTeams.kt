package com.catshi.bob.domain

import com.catshi.bob.exceptions.NoBobTicketException
import com.catshi.bob.exceptions.NotEnoughBobTicketException

class ThisMealBobTeams(
    val vegetarianTeams: List<BobTeam>,
    val anythingTeams: List<BobTeam>,
) {
    override fun toString(): String {
        return listOf(vegetarianTeams, anythingTeams).withIndex()
            .filter { it.value.isNotEmpty() }.joinToString("\n") { asString(it.value, it.index) }.trim()
    }

    private fun asString(teams: List<BobTeam>, index: Int): String {
        val bobStyle = when (index) {
            1 -> "밥"
            0 -> "채식"
            else -> "밥"
        }
        return "${teams[0].leaderTicket.city}의 오늘 ${bobStyle}팀!!! 오늘 1등은 ${
            teams.getFirstTicket()?.user?.name
        }!!!\n" +
                teams.joinToString("\n")
    }
}

private fun List<BobTeam>.getFirstTicket(): BobTicket? {
    this.forEach {
        it.memberTickets.forEach { ticket ->
            if (ticket.isFirst) return ticket
        }
    }
    return null
}