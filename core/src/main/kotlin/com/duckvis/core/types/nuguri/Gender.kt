package com.duckvis.core.types.nuguri

enum class Gender {
  MALE,
  FEMALE,
  ETC,
  UNDEFINED;

  override fun toString(): String {
    return when (this) {
      MALE -> "남"
      FEMALE -> "여"
      ETC -> "기타"
      UNDEFINED -> "알 수 없음"
    }
  }

  companion object {
    fun of(string: String): Gender {
      return when (string) {
        "남자", "남" -> MALE
        "여자", "여" -> FEMALE
        else -> ETC
      }
    }
  }
}