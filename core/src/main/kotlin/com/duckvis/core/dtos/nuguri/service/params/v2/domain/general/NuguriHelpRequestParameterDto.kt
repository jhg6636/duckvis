package com.duckvis.core.dtos.nuguri.service.params.v2.domain.general

import com.duckvis.core.types.nuguri.HelpOption
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto

data class NuguriHelpRequestParameterDto(
  override val userCode: String,
  override val userName: String,
  val helpOption: HelpOption?,
) : NuguriServiceRequestParameterDto(userCode, userName)
