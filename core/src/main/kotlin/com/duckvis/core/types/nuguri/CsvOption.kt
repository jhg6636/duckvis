package com.duckvis.core.types.nuguri

import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException

enum class CsvOption {
  SALARY,
  SEYEOB,
  ;

  companion object {
    fun of(params: List<String>): CsvOption {
      return when {
        params.contains("%인건비") -> SALARY
        params.contains("%세엽") -> SEYEOB
        else -> throw NuguriException(ExceptionType.TYPO)
      }
    }
  }

}