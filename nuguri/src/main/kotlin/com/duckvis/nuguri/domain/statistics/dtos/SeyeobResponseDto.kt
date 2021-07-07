package com.duckvis.nuguri.domain.statistics.dtos

import com.duckvis.core.utils.secondsToHours
import com.duckvis.core.utils.secondsToShortString

data class SeyeobResponseDto(
  val projectName: String,
  val startDay: Int,
  val userName: String,
  val totalSeconds: Int,
  val nightSeconds: Int,
  val holidaySeconds: Int,
  val monthlyTotal: Int,
  val monthlyExtended: Int,
  val projectExtended: Boolean,
) {

  companion object

  val responseString: String
    get() = "$projectName,$startDay,$userName,${totalSeconds.secondsToShortString},${nightSeconds.secondsToShortString}" +
      "${holidaySeconds.secondsToShortString},${monthlyTotal.secondsToShortString},${monthlyExtended.secondsToShortString},${projectExtended}"

}
