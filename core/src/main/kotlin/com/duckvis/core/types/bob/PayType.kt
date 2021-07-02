package com.duckvis.core.types.bob

enum class PayType(val korean: String) {
  MEMBER_SELF("개인결제"),
  SELECTSTAR("법카결제"),
  ALL("");

  companion object {
    fun by(isOver: Boolean): PayType {
      return if (isOver) MEMBER_SELF
      else SELECTSTAR
    }
  }
}