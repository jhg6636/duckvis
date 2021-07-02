package com.duckvis.core.dtos.nuguri

import com.duckvis.core.domain.nuguri.AttendanceCard
import com.duckvis.core.utils.secondsToString

data class WorkTimeDto(
  val all: Int,
  val night: Int,
  val extended: Int,
  val holiday: Int,
) {

  companion object {
    fun of(cards: List<AttendanceCard>): WorkTimeDto {
      val allDurationSeconds = cards.sumBy { card -> card.durationSeconds ?: 0 }
      val extendedDurationSeconds = cards.filter { card -> card.isExtended }.sumBy { card -> card.durationSeconds ?: 0 }
      val holidayDurationSeconds = cards.filter { card -> card.isHoliday }.sumBy { card -> card.durationSeconds ?: 0 }
      val nightDurationSeconds = cards.filter { card -> card.isNight }.sumBy { card -> card.durationSeconds ?: 0 }

      return WorkTimeDto(allDurationSeconds, nightDurationSeconds, extendedDurationSeconds, holidayDurationSeconds)
    }
  }

  val workTimeString: String
    get() {
      val optionalWorkTime = mapOf(Pair("야간", night), Pair("연장", extended), Pair("휴일", holiday))
      return all.secondsToString + if (night == 0 && extended == 0 && holiday == 0) {
        ""
      } else {
        "(${
          optionalWorkTime
            .filter { (workTypeString, duration) ->
              duration != 0
            }
            .map { (workTypeString, duration) ->
              "$workTypeString : ${duration.secondsToString}"
            }
            .joinToString(", ")
        })"
      }
    }

  val hasWorked: Boolean
    get() = this != WorkTimeDto(0, 0, 0, 0)

}