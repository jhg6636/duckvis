package com.duckvis.bob.dtos

import com.duckvis.core.domain.bob.BobTicket

class TicketDto(
  val bobTicket: BobTicket,
  val isFirst: Boolean
) {
}