package com.duckvis.core.dtos.nuguri.service.params.v2.domain.statistics

import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.types.nuguri.CsvOption
import com.duckvis.core.utils.DateTimeMaker

data class NuguriCsvExportRequestParameterDto(
  override val userCode: String,
  override val userName: String,
  val csvOption: CsvOption,
  val mailId: String,
  val month: Int,
) : NuguriServiceRequestParameterDto(userCode, userName) {

  companion object {
    fun of(splitText: List<String>, userCode: String, userName: String): NuguriCsvExportRequestParameterDto {
      val csvOption = CsvOption.of(splitText)
      val mailId = splitText[2]
      val month = if (splitText.size == 4) {
        splitText[3].toInt()
      } else {
        DateTimeMaker.nowDate().monthValue - 1
      }

      return NuguriCsvExportRequestParameterDto(userCode, userName, csvOption, mailId, month)
    }
  }

}
