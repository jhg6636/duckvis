package com.duckvis.core.types.nuguri

import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException

enum class CsvOption {
  SALARY,
  ;

  companion object {
    fun of(params: List<String>): CsvOption {
      return if (params[0] == "%인건비") {
        SALARY
      } else {
        throw NuguriException(ExceptionType.TYPO)
      }
    }
  }

}