package com.duckvis.bob.dtos

import com.duckvis.bob.domain.BobTicket

class TicketDto(
    val bobTicket: BobTicket,
    private val isFirst: Boolean
) {
    override fun toString(): String {
        return if (isFirst) "${bobTicket.user.name}님, 1등하셨네요 너무 귀엽다. ${bobTicket.city} ${bobTicket.bobTimeType}" +
                "${bobTicket.bobStyle}, 맛있게 먹어요"
        else "${bobTicket.city} ${bobTicket.bobTimeType}${bobTicket.bobStyle}, 맛있게 먹어요"
    }
}