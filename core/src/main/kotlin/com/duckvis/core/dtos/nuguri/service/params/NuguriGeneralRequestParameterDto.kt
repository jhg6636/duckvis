package com.duckvis.core.dtos.nuguri.service.params

import com.duckvis.core.types.nuguri.HelpOption

data class NuguriGeneralRequestParameterDto(
  override val userCode: String = "",
  override val userName: String = "",
  val helpOption: HelpOption?,
) : NuguriServiceRequestParameterDto(userCode, userName) {

  companion object {
    fun of(splitText: List<String>): NuguriGeneralRequestParameterDto {
      return NuguriGeneralRequestParameterDto(
        helpOption = HelpOption.of(splitText.getOrNull(1) ?: "")
      )
    }
  }

}
