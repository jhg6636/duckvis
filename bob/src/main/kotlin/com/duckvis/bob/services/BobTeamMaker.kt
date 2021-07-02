package com.duckvis.bob.services

import com.duckvis.core.domain.bob.BobTicket
import com.duckvis.core.domain.bob.ThisMealBobTeams
import com.duckvis.core.exceptions.bob.NotEnoughBobTicketException
import com.duckvis.core.types.bob.PayType

class BobTeamMaker(
  private val allTickets: List<BobTicket>,
  private val teamSortStrategy: TeamSortStrategy,
) {

  private fun checkLateTeamExists(lateEatTickets: List<BobTicket>): Boolean {
    return lateEatTickets.size >= 2
  }

  // 얘네끼린 같은 팀이야 조건 grouping Map<조건, List<인간>>
  // 채식, [5명] 안채식 [7명] // split & merge
  // C를 D로 나눈다
  // A명을 B개의 그룹으로 나누는 로직
  // N명을 M개의 실제 팀으로 나누는 로직
  // (채식, 지원) (채식, 미지원) (블바,블라) (블라, 블라)
  // + early return을 적절히 섞으면 이런 복잡한 로직을 더 잘 짤 수 있지 않을까?
  // TODO 지금 바꾸자 절대 아닙니다 ㅋㅋㅋ
  fun make(decisionLogic: TeamDecisionLogic, payType: PayType): ThisMealBobTeams {
    val vegetarianTickets = allTickets.filter { it.isVegetarian }
    val anythingTickets = allTickets.filter { it.isAnything }
    val lateEatTickets = allTickets.filter { it.isLateEat }
    return when {
      allTickets.size < decisionLogic.minimumTeamCount -> throw NotEnoughBobTicketException()
      (anythingTickets + lateEatTickets).isEmpty() -> ThisMealBobTeams(
        vegetarianTeams = decisionLogic.decide(allTickets, teamSortStrategy),
        anythingTeams = emptyList(),
        lateEatTeams = emptyList(),
        payType
      )
      vegetarianTickets.size < decisionLogic.minimumTeamCount || anythingTickets.size < decisionLogic.minimumTeamCount -> {
        if (anythingTickets.isEmpty()) {
          if (checkLateTeamExists(lateEatTickets) && vegetarianTickets.size >= decisionLogic.minimumTeamCount) {
            ThisMealBobTeams(
              vegetarianTeams = decisionLogic.decide(vegetarianTickets, teamSortStrategy),
              anythingTeams = emptyList(),
              lateEatTeams = decisionLogic.decide(lateEatTickets, teamSortStrategy),
              payType
            )
          } else {
            ThisMealBobTeams(
              vegetarianTeams = emptyList(),
              anythingTeams = decisionLogic.decide(allTickets, teamSortStrategy),
              lateEatTeams = emptyList(),
              payType
            )
          }
        } else if (checkLateTeamExists(lateEatTickets) && (vegetarianTickets + anythingTickets).size >= decisionLogic.minimumTeamCount) {
          ThisMealBobTeams(
            vegetarianTeams = emptyList(),
            anythingTeams = decisionLogic.decide(anythingTickets + vegetarianTickets, teamSortStrategy),
            lateEatTeams = decisionLogic.decide(lateEatTickets, teamSortStrategy),
            payType
          )
        } else {
          ThisMealBobTeams(
            vegetarianTeams = emptyList(),
            anythingTeams = decisionLogic.decide(allTickets, teamSortStrategy),
            lateEatTeams = emptyList(),
            payType
          )
        }
      }
      checkLateTeamExists(lateEatTickets) -> ThisMealBobTeams(
        vegetarianTeams = decisionLogic.decide(vegetarianTickets, teamSortStrategy),
        anythingTeams = decisionLogic.decide(anythingTickets, teamSortStrategy),
        lateEatTeams = decisionLogic.decide(lateEatTickets, teamSortStrategy),
        payType
      )
      else -> ThisMealBobTeams(
        vegetarianTeams = decisionLogic.decide(vegetarianTickets, teamSortStrategy),
        anythingTeams = decisionLogic.decide(anythingTickets + lateEatTickets, teamSortStrategy),
        lateEatTeams = emptyList(),
        payType
      )
    }
  }
}