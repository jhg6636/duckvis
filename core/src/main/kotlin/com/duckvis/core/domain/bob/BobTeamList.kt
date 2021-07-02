package com.duckvis.core.domain.bob

import com.duckvis.core.types.bob.PayType

data class BobTeamList(
  val bobTeams: List<BobTeam>
) {
  fun asString(index: Int, payType: PayType): String {
    val bobStyle = when (index) {
      2 -> "늦먹"
      1 -> "밥"
      0 -> "채식"
      else -> "밥"
    }
    val firstTicketAnnouncement = when (index) {
      1, 0 -> "오늘 1등은 ${getFirstTicket?.userName ?: "없습니다"}!!!"
      else -> ""
    }
    return "${bobTeams[0].leaderTicket.city}의 오늘 ${bobTeams[0].leaderTicket.bobTimeType.korean} $bobStyle ${payType.korean}팀!!! " +
      "$firstTicketAnnouncement\n" + bobTeams.joinToString("\n") { team -> team.bobTeamString }
  }

  private val getFirstTicket: BobTicket?
    get() {
      bobTeams.forEach {
        it.memberTickets.forEach { ticket ->
          if (ticket.isFirst) return ticket
        }
      }
      return null
    }
}