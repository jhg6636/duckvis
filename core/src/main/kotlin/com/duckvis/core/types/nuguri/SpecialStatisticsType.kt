package com.duckvis.core.types.nuguri

enum class SpecialStatisticsType {
  WEEKLY,
  MONTHLY,
  NORMAL,
  ALL_PROJECT,
  ;

  companion object {
    fun of(text: String): SpecialStatisticsType {
      if (text.contains("^모든플젝")) {
        return ALL_PROJECT
      }
      return when (text) {
        "!월간통계", "!ㅇㄱㅌㄱ" -> MONTHLY
        "!주간통계", "!ㅈㄱㅌㄱ" -> WEEKLY
        else -> NORMAL
      }
    }
  }

}