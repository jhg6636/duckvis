package com.duckvis.nuguri.domain.statistics.dtos

import com.duckvis.core.types.nuguri.SpecialStatisticsType
import com.duckvis.nuguri.domain.attendance.dtos.MemberWorkDuration

data class AdminStatisticsResponseDto(
  val memberWorkDurations: List<MemberWorkDuration>,
  val type: SpecialStatisticsType,
) {

  val responseString: String
    get() = "$headMessage\n${
      memberWorkDurations.joinToString("\n") { memberWorkDuration ->
        memberWorkDuration.oneMemberString
      }
    }\n만큼 일하셨어요~"

  private val headMessage: String
    get() = when (type) {
      SpecialStatisticsType.MONTHLY -> "어디 봅시다...\n이번달 통계에요~\n"
      SpecialStatisticsType.WEEKLY -> "음... 찾았어요!\n이번주 통계에요~\n"
      SpecialStatisticsType.NORMAL -> "어...앗!\n해당 기간의 통계에요~\n"
      else -> ""
    }

}