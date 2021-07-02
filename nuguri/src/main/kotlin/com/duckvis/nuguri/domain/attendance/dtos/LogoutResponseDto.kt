package com.duckvis.nuguri.domain.attendance.dtos

data class LogoutResponseDto(
  val projectFullName: String? = null,
  val isOverTwelve: Boolean? = null,
)
