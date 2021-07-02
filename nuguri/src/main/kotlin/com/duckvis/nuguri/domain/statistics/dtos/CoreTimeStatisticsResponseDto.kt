package com.duckvis.nuguri.domain.statistics.dtos

data class CoreTimeStatisticsResponseDto(
  val responseString: String,
  val notEnoughResponsesForDM: List<CoreTimeEndResponse>,
) {
}