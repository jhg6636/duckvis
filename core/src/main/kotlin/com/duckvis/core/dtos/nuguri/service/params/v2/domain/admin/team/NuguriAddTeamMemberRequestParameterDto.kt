package com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.team

import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto

data class NuguriAddTeamMemberRequestParameterDto(
  override val userCode: String,
  override val userName: String,
  val teamName: String,
  val memberName: String,
) : NuguriServiceRequestParameterDto(userCode, userName)
