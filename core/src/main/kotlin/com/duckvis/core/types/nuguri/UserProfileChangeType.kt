package com.duckvis.core.types.nuguri

import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException

enum class UserProfileChangeType {
  NAME,
  GENDER,
  BIRTH_DATE,
  JOIN_DATE,
  STATUS_MESSAGE,
  DISCOUNT_OPTION,
  TARGET_WORK_HOUR;

  companion object {
    fun of(korean: String): UserProfileChangeType {
      return when (korean) {
        "이름개명" -> NAME
        "성별" -> GENDER
        "생일" -> BIRTH_DATE
        "입사일" -> JOIN_DATE
        "상메", "상태메세지", "상태메시지" -> STATUS_MESSAGE
        "부족하면" -> DISCOUNT_OPTION
        "근무시간", "근무목표" -> TARGET_WORK_HOUR
        else -> throw NuguriException(ExceptionType.USER_PROFILE_CHANGE_TYPE_TYPO)
      }
    }
  }
}