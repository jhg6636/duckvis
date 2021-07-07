package com.duckvis.core.dtos.nuguri.service.params.v2.domain.statistics

import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.core.utils.dayToDates
import java.time.LocalDate

data class NuguriCoreTimeStatisticsRequestParameterDto(
  override val userCode: String,
  override val userName: String,
  val date: LocalDate,
) : NuguriServiceRequestParameterDto(userCode, userName) {

  companion object {
    fun of(splitText: List<String>, userCode: String, userName: String): NuguriCoreTimeStatisticsRequestParameterDto {
      val date = when (splitText.size) {
        1 -> DateTimeMaker.nowDate()
        2 -> DateTimeMaker.stringToDate(splitText[1])
        else -> throw NuguriException(ExceptionType.TYPO)
      }

      return NuguriCoreTimeStatisticsRequestParameterDto(userCode, userName, date)
    }
  }

}
