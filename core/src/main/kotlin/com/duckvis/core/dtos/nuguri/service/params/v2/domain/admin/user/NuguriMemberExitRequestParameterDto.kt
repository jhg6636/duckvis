package com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.user

import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto

data class NuguriMemberExitRequestParameterDto(
  override val userCode: String,
  override val userName: String,
  val memberName: String,
) : NuguriServiceRequestParameterDto(userCode, userName)
