package com.duckvis.nuguri.domain.statistics.dtos

import com.duckvis.core.domain.nuguri.UserTeam
import com.duckvis.core.dtos.nuguri.WorkTypeDto
import com.duckvis.core.types.nuguri.SpecialStatisticsType
import com.duckvis.core.types.nuguri.service.params.NuguriStatisticsRequestParameterDto
import java.time.LocalDateTime

data class StatisticsRequestDto(
  val userCode: String,
  val userTeam: UserTeam?,
  val isAdmin: Boolean,
  val type: SpecialStatisticsType,
  val startDateTime: LocalDateTime,
  val endDateTime: LocalDateTime,
  val workType: WorkTypeDto,
  val projectName: String?,
  val adminStatisticsRequestDto: AdminStatisticsRequestDto,
) {

  companion object {
    fun of(params: NuguriStatisticsRequestParameterDto): StatisticsRequestDto {
      val adminStatisticsRequestDto = AdminStatisticsRequestDto.ofV2(params)
      return StatisticsRequestDto(
        userCode = params.userCode,
        userTeam = params.userTeam,
        isAdmin = adminStatisticsRequestDto.isAdminStatistics,
        type = params.statisticsType,
        startDateTime = params.from,
        endDateTime = params.to,
        workType = params.workTypeDto,
        projectName = params.projectNameOrNickname,
        adminStatisticsRequestDto = adminStatisticsRequestDto
      )
    }
  }

}