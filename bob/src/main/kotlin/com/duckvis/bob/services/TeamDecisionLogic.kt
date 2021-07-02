package com.duckvis.bob.services

import com.duckvis.core.domain.bob.BobTeam
import com.duckvis.core.domain.bob.BobTicket

interface TeamDecisionLogic {

  companion object {
    const val BIG_BIG_BIG_NUMBER = 1000
  }

  /**
   * 충분히 많이 시도해봤는데도, 팀이 안나오면 확률적으로 팀이 매칭될 수 없다고 보고
   * 예외를 날린다.
   */
  fun decide(tickets: List<BobTicket>, teamSortStrategy: TeamSortStrategy): List<BobTeam> {
    return teamSortStrategy
      .sort(tickets)
      .chunkedForTeams(chunkSize)
      .map { BobTeam(it) }
  }

  val minimumCount: Int
  val minimumTeamCount: Int
  val chunkSize: Int

  private fun List<BobTicket>.chunkedForTeams(chunkSize: Int): List<List<BobTicket>> {
    return this
      .chunked(chunkSize)
      .let {
        if (it.last().size == 1)
          it.subList(0, it.beforeLastIndex) + (it.beforeLast + it.last()).chunked(2)
        else
          it
      }
  }

}

class CovidTeamDecisionLogic : TeamDecisionLogic {

  override val minimumCount = 3

  override val minimumTeamCount: Int = 2

  override val chunkSize: Int = 3
}

class FifoTeamDecisionLogic : TeamDecisionLogic {

  override val minimumCount: Int = 3

  override val minimumTeamCount: Int = 3

  override val chunkSize: Int = 3
}

class WeekdayLunchOrWeekendTeamDecisionLogic : TeamDecisionLogic {
  override val minimumCount: Int = 3

  override val minimumTeamCount: Int = 4

  override val chunkSize: Int = 4

}

class WeekdayDinnerTeamDecisionLogic : TeamDecisionLogic {

  override val minimumCount: Int = 4

  override val minimumTeamCount: Int = 4

  override val chunkSize: Int = 4

}

class WeekendTeamDecisionLogic : TeamDecisionLogic {

  override val minimumCount: Int = 2

  override val minimumTeamCount: Int = 2

  override val chunkSize: Int = 2
}

class DaejeonTeamDecisionLogic : TeamDecisionLogic {

  override val minimumCount: Int = 2

  override val minimumTeamCount: Int = 2

  override val chunkSize: Int = 2
}

// ListUtils
private val <T> List<T>.beforeLastIndex: Int
  get() = this.lastIndex - 1

private val <T> List<T>.beforeLast: T
  get() = this[beforeLastIndex]