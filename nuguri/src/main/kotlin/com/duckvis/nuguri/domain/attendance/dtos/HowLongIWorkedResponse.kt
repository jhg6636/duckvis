package com.duckvis.nuguri.domain.attendance.dtos

import com.duckvis.core.utils.secondsToString

data class HowLongIWorkedResponse(
  val nowWorkSeconds: Int,
  val todayWorkSeconds: Int,
) {
  override fun toString(): String {
    return ":mantelpiece_clock:지금 ${nowWorkSeconds.secondsToString}째 근무중이니까...\n" +
      ":mantelpiece_clock:오늘 ${todayWorkSeconds.secondsToString}째 근무중인거네요~"
  }
}
