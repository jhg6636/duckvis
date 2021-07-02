package com.duckvis.core.types.nuguri

enum class HelpOption {
  PROJECT,
  ADMINISTRATION,
  ATTENDANCE,
  STATISTICS,
  HELP_OPTIONS,
  ETC;

  companion object {
    fun of(korean: String): HelpOption {
      return when (korean) {
        "%플젝" -> PROJECT
        "%사원관리" -> ADMINISTRATION
        "%출퇴근" -> ATTENDANCE
        "%통계" -> STATISTICS
        "%기타" -> ETC
        else -> HELP_OPTIONS
      }
    }
  }
}