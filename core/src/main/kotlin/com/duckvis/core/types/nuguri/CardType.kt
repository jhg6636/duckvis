package com.duckvis.core.types.nuguri

enum class CardType {

  WORK,
  MISTAKE;

  override fun toString(): String {
    return when (this) {
      WORK -> "근무"
      MISTAKE -> "실수"
    }
  }

}
