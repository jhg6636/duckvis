package com.duckvis.core.domain.bob

class BobTeam(
  tickets: List<BobTicket>,
) {

  val leaderTicket: BobTicket = if (tickets[0].isFirst) {
    if (tickets[1].isFirst) {
      if (tickets.size == 2) {
        tickets.maxByOrNull { it.time } ?: tickets[0]
      } else {
        tickets[2]
      }
    } else {
      tickets[1]
    }
  } else {
    tickets[0]
  }
  val memberTickets: List<BobTicket> = tickets

  private val isNotAvailable: Boolean
    get() {
      val nameList = this.memberTickets.map { ticket -> ticket.userName }
      return ("솔아" in nameList && "남길" in nameList)
        || ("솔아" in nameList && "바롬" in nameList)
        || ("유리" in nameList && "남길" in nameList)
        || ("유리" in nameList && "바롬" in nameList)
    }

  val isAvailable: Boolean
    get() = !this.isNotAvailable

  val bobTeamString: String
    get() = "${this.leaderTicket.userName} 팀 : ${
      this.memberTickets.joinToString(" ") { ticket ->
        ticket.userTagString
      }
    }"
}