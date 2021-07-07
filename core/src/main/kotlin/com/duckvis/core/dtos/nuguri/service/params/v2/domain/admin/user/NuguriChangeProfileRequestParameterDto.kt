package com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.user

import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto

data class NuguriChangeProfileRequestParameterDto(
  override val userCode: String,
  override val userName: String,
  val field: String,
  val content: String,
) : NuguriServiceRequestParameterDto(userCode, userName)
