package com.duckvis.nuguri.services

import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMajorType
import com.duckvis.core.types.nuguri.service.params.*
import com.duckvis.nuguri.dtos.ServiceRequestDtoV2

interface NuguriServiceV2 {

  val majorType: CommandMajorType

  fun response(serviceRequestDto: ServiceRequestDtoV2): String
  fun parameterCheck(requestParameterDto: NuguriServiceRequestParameterDto) {
    val isThrow = when (majorType) {
      CommandMajorType.ADMIN -> requestParameterDto !is NuguriAdminRequestParameterDto
      CommandMajorType.ATTENDANCE -> requestParameterDto !is NuguriAttendanceRequestParameterDto
      CommandMajorType.STATISTICS -> requestParameterDto !is NuguriStatisticsRequestParameterDto
      CommandMajorType.GENERAL -> requestParameterDto !is NuguriGeneralRequestParameterDto
    }

    if (isThrow) {
      throw NuguriException(ExceptionType.TYPO)
    }
  }

}