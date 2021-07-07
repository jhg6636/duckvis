package com.duckvis.nuguri.domain.statistics.service.v2

import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.types.nuguri.service.CommandMajorType
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.statistics.NuguriStatisticsRequestParameterDto
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.nuguri.dtos.ServiceRequestDtoV2
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("STATISTICS_V2")
class StatisticsServiceV2(
  @Autowired private val normalStatisticsAction: NormalStatisticsActionV2,
  @Autowired private val adminStatisticsAction: AdminStatisticsActionV2,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.STATISTICS

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriStatisticsRequestParameterDto

    return if (params.isAdmin) {
      adminStatisticsAction.act(params)
    } else {
      normalStatisticsAction.act(params)
    }
  }

}