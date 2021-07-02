package com.duckvis.nuguri.domain.statistics.dtos

import com.duckvis.core.utils.secondsToHours
import com.duckvis.core.utils.secondsToShortString

data class SalaryResponseDto(
  val teamName: String,
  val salaryCode: Long,
  val isManager: Boolean,
  val name: String,
  val allDurationSeconds: Int,
  val extendedDurationSeconds: Int,
  val holidayDurationSeconds: Int,
  val nightDurationSeconds: Int,
  val dayOff: Int,
  val dayOffSick: Int,
  val lessThanTarget: Boolean
) {

  private val allDurationHours: Double
    get() = allDurationSeconds.secondsToHours

  private val extendedDurationHours: Double
    get() = extendedDurationSeconds.secondsToHours

  private val holidayDurationHours: Double
    get() = holidayDurationSeconds.secondsToHours

  private val nightDurationHours: Double
    get() = nightDurationSeconds.secondsToHours

  val basicString: String
    get() = "$teamName,$name,${allDurationSeconds.secondsToShortString},${extendedDurationSeconds.secondsToShortString}," +
      "${holidayDurationSeconds.secondsToShortString},${nightDurationSeconds.secondsToShortString},$lessThanTarget"

  val additionalString: String
    get() = "$salaryCode,$name,${allDurationSeconds.secondsToShortString},${extendedDurationSeconds.secondsToShortString}," +
      "${holidayDurationSeconds.secondsToShortString},${nightDurationSeconds.secondsToShortString},$lessThanTarget," +
      "$allDurationHours,$extendedDurationHours,$holidayDurationHours,$nightDurationHours,${allDurationSeconds - extendedDurationSeconds}," +
      "$dayOff,$dayOffSick"

}
