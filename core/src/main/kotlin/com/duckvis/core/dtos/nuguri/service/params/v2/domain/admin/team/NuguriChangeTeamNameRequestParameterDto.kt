package com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.team

import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto

data class NuguriChangeTeamNameRequestParameterDto(
  override val userCode: String,
  override val userName: String,
  val originalName: String,
  val newName: String,
) : NuguriServiceRequestParameterDto(userCode, userName)
