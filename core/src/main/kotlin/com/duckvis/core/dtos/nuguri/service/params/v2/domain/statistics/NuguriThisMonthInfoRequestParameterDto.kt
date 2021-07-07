package com.duckvis.core.dtos.nuguri.service.params.v2.domain.statistics

import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.utils.DateTimeMaker

data class NuguriThisMonthInfoRequestParameterDto(
  override val userCode: String,
  override val userName: String,
  val month: Int,
  val year: Int,
) : NuguriServiceRequestParameterDto(userCode, userName) {

  companion object {
    fun of(splitText: List<String>, userCode: String, userName: String): NuguriThisMonthInfoRequestParameterDto {
      val month = when (splitText.size) {
        2, 3 -> splitText[1].toInt()
        else -> DateTimeMaker.nowDate().monthValue
      }
      val year = when (splitText.size) {
        3 -> splitText[2].toInt()
        else -> DateTimeMaker.nowDate().year
      }

      val result = NuguriThisMonthInfoRequestParameterDto(userCode, userName, month, year)
      result.checkMonthTypo()

      return result
    }
  }

  fun checkMonthTypo() {
    if (month > 12 || month < 1) {
      throw NuguriException(ExceptionType.MONTH_TYPO)
    }
  }

}