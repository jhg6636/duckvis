package com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.holiday

import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.utils.dayToDates
import java.time.LocalDate

data class NuguriSetHolidaysRequestParameterDto(
  override val userCode: String,
  override val userName: String,
  val dates: List<LocalDate>,
) : NuguriServiceRequestParameterDto(userCode, userName) {

  companion object {
    fun of(splitText: List<String>, userCode: String, userName: String): NuguriSetHolidaysRequestParameterDto {
      val dates = splitText[1].dayToDates

      return NuguriSetHolidaysRequestParameterDto(userCode, userName, dates)
    }
  }

}
