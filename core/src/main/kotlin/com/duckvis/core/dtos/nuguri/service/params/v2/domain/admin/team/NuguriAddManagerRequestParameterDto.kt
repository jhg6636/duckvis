package com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.team

import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto

data class NuguriAddManagerRequestParameterDto(
  override val userCode: String,
  override val userName: String,
  val teamName: String,
  val managerName: String,
) : NuguriServiceRequestParameterDto(userCode, userName)
