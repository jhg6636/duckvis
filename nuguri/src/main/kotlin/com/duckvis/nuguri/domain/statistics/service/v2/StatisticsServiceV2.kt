package com.duckvis.nuguri.domain.statistics.service.v2

import com.duckvis.core.types.nuguri.service.CommandMajorType
import com.duckvis.core.types.nuguri.service.params.NuguriStatisticsRequestParameterDto
import com.duckvis.nuguri.dtos.ServiceRequestDtoV2
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StatisticsServiceV2(
  @Autowired private val normalStatisticsAction: NormalStatisticsActionV2,
  @Autowired private val adminStatisticsAction: AdminStatisticsActionV2,
) : NuguriServiceV2 {

  override val majorType: CommandMajorType
    get() = CommandMajorType.STATISTICS

  override fun response(serviceRequestDto: ServiceRequestDtoV2): String {
    val params = serviceRequestDto.parameter
    parameterCheck(params)
    params as NuguriStatisticsRequestParameterDto

    return if (params.isAdmin) {
      adminStatisticsAction.act(params)
    } else {
      normalStatisticsAction.act(params)
    }
  }

}