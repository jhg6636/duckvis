package com.duckvis.core.dtos.nuguri.service.params.v2.domain

import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto

data class NuguriServiceRequestNoParameterDto(
  override val userCode: String,
  override val userName: String,
) : NuguriServiceRequestParameterDto(userCode, userName)