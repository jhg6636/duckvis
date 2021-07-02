package com.duckvis.nuguri.services

import com.duckvis.nuguri.dtos.ServiceRequestDto
import org.springframework.stereotype.Service

@Service("NOTHING")
class NothingService(
) : NuguriService {
  override val maximumRequestParams: Int = 0
  override val minimumRequestParams: Int = 0
  override val minimumPermission: ServicePermission = ServicePermission.EVERYBODY
  override fun response(serviceRequestDto: ServiceRequestDto): String {
    return ""
  }
}