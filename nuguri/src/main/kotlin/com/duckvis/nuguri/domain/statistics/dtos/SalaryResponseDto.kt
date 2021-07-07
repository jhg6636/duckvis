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
  val overTarget: Boolean
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
    //팀명,이름,총근로시간,연장,휴일,야간,총근로시간-연장,총근로시간,연장,휴일,야간,총근로시간-연장,휴가 및 기타,병가,연장확인,비고
    get() {
      return "$teamName,$name,${allDurationSeconds.secondsToShortString},${extendedDurationSeconds.secondsToShortString}," +
        "${holidayDurationSeconds.secondsToShortString},${nightDurationSeconds.secondsToShortString}," +
        "${(allDurationSeconds - extendedDurationSeconds).secondsToShortString}$allDurationHours,$extendedDurationHours," +
        "$holidayDurationHours$nightDurationHours,${allDurationHours-extendedDurationHours}$dayOff,$dayOffSick,$overTarget"
    }

  val additionalString: String
  //"사원코드,이름,총근로시간,연장,휴일,야간,총근로시간-연장,총근로시간,연장,휴일,야간,총근로시간-연장,휴가 및 기타,병가,연장확인,비고
    get() = "$salaryCode,$name,${allDurationSeconds.secondsToShortString},${extendedDurationSeconds.secondsToShortString}," +
      "${holidayDurationSeconds.secondsToShortString},${nightDurationSeconds.secondsToShortString}," +
    "${(allDurationSeconds-extendedDurationSeconds).secondsToShortString}," +
      "$allDurationHours,$extendedDurationHours,$holidayDurationHours,$nightDurationHours,${allDurationHours - extendedDurationHours}," +
      "$dayOff,$dayOffSick,$overTarget"

}
