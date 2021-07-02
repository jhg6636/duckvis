package com.duckvis.core.domain.bob

data class ThisMealBobTeamsList(
  val teams: List<ThisMealBobTeams>
) {
  val isAvailable: Boolean
    get() = teams.all { thisMealBobTeams ->
      thisMealBobTeams.allTeams.all { team ->
        team.isAvailable
      }
    }
}