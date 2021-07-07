package com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.team

import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto

data class NuguriTeamInfoRequestParameterDto(
  override val userCode: String,
  override val userName: String,
  val name: String,
) : NuguriServiceRequestParameterDto(userCode, userName)
