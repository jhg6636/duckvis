package com.duckvis.core.types.nuguri

import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException

enum class DiscountOption {
  PAY,
  VACATION;

  override fun toString(): String {
    return when (this) {
      PAY -> "월급"
      VACATION -> "휴가"
    }
  }

  companion object {
    fun of(string: String): DiscountOption {
      return when (string) {
        "월급에서" -> PAY
        "휴가에서" -> VACATION
        else -> throw NuguriException(ExceptionType.DISCOUNT_OPTION_TYPO)
      }
    }
  }
}