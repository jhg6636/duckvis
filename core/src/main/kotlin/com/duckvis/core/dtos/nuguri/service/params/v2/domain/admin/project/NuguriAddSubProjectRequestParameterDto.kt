package com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.project

import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto

data class NuguriAddSubProjectRequestParameterDto(
  override val userCode: String,
  override val userName: String,
  val name: String,
  val nickname: String,
  val projectName: String,
) : NuguriServiceRequestParameterDto(userCode, userName)