package com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.project

import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto

data class NuguriSubProjectListRequestParameterDto(
  override val userCode: String,
  override val userName: String,
  val projectNameOrNickname: String,
) : NuguriServiceRequestParameterDto(userCode, userName)
