package com.duckvis.core.types.bob

enum class BobStyleType {
  ANYTHING,
  VEGETARIAN,
  LATE_EAT;

  override fun toString(): String {
    return when (this) {
      ANYTHING -> "밥"
      VEGETARIAN -> "채식"
      LATE_EAT -> "늦먹"
    }
  }
}