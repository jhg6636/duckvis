package com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.user

import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto

data class NuguriSetSickDayOffRequestParameterDto(
  override val userCode: String,
  override val userName: String,
  val days: Int,
) : NuguriServiceRequestParameterDto(userCode, userName)
