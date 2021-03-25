package com.duckvis.bob.types

import com.duckvis.bob.exceptions.NotTicketTimeException
import java.time.LocalDateTime

enum class BobTimeType(val startHour: Int, val endHour: Int) {
    BREAKFAST(22, 23),
    LUNCH(1, 2),
    DINNER(7, 8);

    companion object {
        fun of(now: LocalDateTime): BobTimeType {
            return values().firstOrNull { it.isBobTimeIn(now.hour) }
                ?: throw NotTicketTimeException()

        }
    }

    private fun isBobTimeIn(nowHour: Int): Boolean {
        return nowHour in startHour until endHour
    }

    override fun toString(): String {
        return when (this) {
            BREAKFAST -> "아침"
            LUNCH -> "점심"
            DINNER -> "저녁"
        }
    }
}