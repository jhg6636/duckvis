package com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.project

import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto

data class NuguriDeleteSubProjectRequestParameterDto(
  override val userCode: String,
  override val userName: String,
  val subProjectName: String,
  val projectName: String,
) : NuguriServiceRequestParameterDto(userCode, userName)
