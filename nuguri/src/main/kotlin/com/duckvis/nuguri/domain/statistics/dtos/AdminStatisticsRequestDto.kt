package com.duckvis.nuguri.domain.statistics.dtos

import com.duckvis.core.types.nuguri.service.params.NuguriStatisticsRequestParameterDto

data class AdminStatisticsRequestDto(
  val projectName: String? = null,
  val teamName: String? = null,
  val memberName: String? = null,
  val isEverybody: Boolean = false,
) {

  companion object {

    fun of(params: List<String>): AdminStatisticsRequestDto {
      val projectName = params.singleOrNull { param ->
        param.startsWith("^") &&
          !param.startsWith("^^") &&
          param != "^전체"
      }
        ?.substringAfter("^")
      val teamName = params.singleOrNull { param ->
        param.startsWith("^^") &&
          !param.startsWith("^^^")
      }
        ?.substringAfter("^^")
      val memberName = params.singleOrNull { param -> param.startsWith("^^^") }
        ?.substringAfter("^^^")

      return AdminStatisticsRequestDto(projectName, teamName, memberName, params.contains("^전체"))
    }

    fun ofV2(params: NuguriStatisticsRequestParameterDto): AdminStatisticsRequestDto {
      return AdminStatisticsRequestDto(
        params.projectNameOrNickname,
        params.teamName,
        params.memberName,
        params.isEveryBody
      )
    }

  }

  val isAdminStatistics: Boolean
    get() = (teamName != null || memberName != null || isEverybody) ||
      !(projectName != null && teamName == null && memberName == null && !isEverybody)

}