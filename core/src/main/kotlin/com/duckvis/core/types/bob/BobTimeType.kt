package com.duckvis.core.types.bob

import com.duckvis.core.exceptions.bob.BobTimeStringException
import com.duckvis.core.exceptions.bob.NotTicketTimeException
import com.duckvis.core.exceptions.bob.TooLateException
import java.time.LocalDateTime

enum class BobTimeType(
  val startHour: Int,
  val endHour: Int,
  val korean: String
) {
  BREAKFAST(22, 23, "아침"),
  LUNCH(1, 2, "점심"),
  DINNER(7, 8, "저녁");

  companion object {
    fun of(now: LocalDateTime): BobTimeType {
      return values()
        .firstOrNull { bobTimeType -> bobTimeType.isBobTimeIn(now.hour) }
        ?: throw NotTicketTimeException()
    }

    fun payRecordTime(now: LocalDateTime): BobTimeType {
      return when (now.hour) {
        2, 3, 4, 5, 6 -> LUNCH
        8, 9, 10, 11, 12 -> DINNER
        else -> throw TooLateException()
      }
    }

    fun fromString(string: String): BobTimeType {
      return values()
        .firstOrNull { bobTimeType -> bobTimeType.korean == string }
        ?: throw BobTimeStringException()
    }
  }

  private fun isBobTimeIn(nowHour: Int): Boolean {
    return nowHour in startHour until endHour
  }
}