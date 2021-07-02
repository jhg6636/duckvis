package com.duckvis.bob.dtos.covid

import com.duckvis.core.domain.bob.BobTicket

data class TicketCountDto(
  val ticket: BobTicket,
  val count: Int,
)