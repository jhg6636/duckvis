package com.duckvis.nuguri.services

import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.types.nuguri.service.CommandMinorType
import org.springframework.stereotype.Service

@Service("NOTHING_V2")
class NothingServiceV2 : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.NOTHING

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    return ""
  }

}