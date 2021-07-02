package com.duckvis.nuguri.domain.admin.dtos

import com.duckvis.core.domain.shared.User

data class TeamInformationDto(
  val name: String,
  val manager: List<User>,
  val members: List<User>,
) {
  override fun toString(): String {
    return "${name}팀의 정보에요~\n" +
      ":dark_sunglasses:팀 매니저: ${manager.joinToString(", ") { it.name }}\n" +
      ":busts_in_silhouette:팀 멤버들\n" +
      "${members.joinToString("\n") { it.name }}"
  }
}
