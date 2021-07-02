package com.duckvis.core.domain.nuguri

import com.duckvis.core.domain.BaseDuckvisEntity
import com.duckvis.core.types.nuguri.UserTeamLevel
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
class UserTeam(
  val userId: Long,
  val teamId: Long,

  @Enumerated(EnumType.STRING)
  private var level: UserTeamLevel = UserTeamLevel.MEMBER,
) : BaseDuckvisEntity() {

  val isManager: Boolean
    get() = this.level == UserTeamLevel.MANAGER

  fun isSameUser(userId: Long): Boolean {
    return this.userId == userId
  }

  fun setLevel(level: UserTeamLevel) {
    this.level = level
  }

}