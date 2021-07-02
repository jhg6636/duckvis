package com.duckvis.core.domain.bob

import com.duckvis.core.types.bob.PayType

class ThisMealBobTeams(
  val vegetarianTeams: List<BobTeam>,
  val anythingTeams: List<BobTeam>,
  val lateEatTeams: List<BobTeam>,
  val payType: PayType,
) {

  val isSelf: Boolean
    get() = this.payType == PayType.MEMBER_SELF

  val allTeams: List<BobTeam> = vegetarianTeams + anythingTeams + lateEatTeams

  val asString: String
    get() = listOf(this.vegetarianTeams, this.anythingTeams, this.lateEatTeams)
      .withIndex()
      .filter { it.value.isNotEmpty() }
      .joinToString("\n") { BobTeamList(it.value).asString(it.index, this.payType) }.trim()
}