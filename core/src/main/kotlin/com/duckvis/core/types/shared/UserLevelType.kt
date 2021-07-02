package com.duckvis.core.types.shared

enum class UserLevelType {
  NORMAL,
  ADMIN,
  EXIT;

  val isAdmin: Boolean
    get() = this == ADMIN

  val isExit: Boolean
    get() = this == EXIT
}