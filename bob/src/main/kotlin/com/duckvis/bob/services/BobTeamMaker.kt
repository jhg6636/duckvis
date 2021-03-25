package com.duckvis.bob.services

import com.duckvis.bob.domain.ThisMealBobTeams
import com.duckvis.bob.exceptions.NotEnoughBobTicketException
import com.duckvis.bob.types.TeamAvailabilityType
import com.duckvis.bob.domain.BobTicket

class BobTeamMaker(
    private val allTickets: List<BobTicket>,
    private val teamSortStrategy: TeamSortStrategy,
) {

    private val vegetarianTickets = allTickets.filter { it.isVegetarian }
    private val anythingTickets = allTickets.filter { it.isAnything }

    private fun checkTeamAvailability(decisionLogic: TeamDecisionLogic): TeamAvailabilityType {
        return if (vegetarianTickets.size >= decisionLogic.minimumTeamCount && anythingTickets.size >= decisionLogic.minimumTeamCount) {
            TeamAvailabilityType.ALL
        } else if (vegetarianTickets.size >= decisionLogic.minimumTeamCount && anythingTickets.isEmpty()) {
            TeamAvailabilityType.VEGETARIAN_ONLY
        } else if (anythingTickets.size >= decisionLogic.minimumTeamCount ||
            allTickets.size >= decisionLogic.minimumCount
        ) {
            TeamAvailabilityType.ANYTHING_ONLY
        } else {
            TeamAvailabilityType.NONE
        }
    }

    fun make(decisionLogic: TeamDecisionLogic): ThisMealBobTeams {
        return when (checkTeamAvailability(decisionLogic)) {
            TeamAvailabilityType.NONE -> {
                throw NotEnoughBobTicketException()
            }
            TeamAvailabilityType.ALL -> {
                ThisMealBobTeams(
                    vegetarianTeams = decisionLogic.decide(vegetarianTickets, teamSortStrategy),
                    anythingTeams = decisionLogic.decide(anythingTickets, teamSortStrategy),
                )
            }
            TeamAvailabilityType.VEGETARIAN_ONLY -> {
                ThisMealBobTeams(
                    vegetarianTeams = decisionLogic.decide(vegetarianTickets, teamSortStrategy),
                    anythingTeams = listOf()
                )
            }
            TeamAvailabilityType.ANYTHING_ONLY -> {
                ThisMealBobTeams(
                    vegetarianTeams = listOf(),
                    anythingTeams = decisionLogic.decide(allTickets, teamSortStrategy)
                )
            }
        }
    }

}