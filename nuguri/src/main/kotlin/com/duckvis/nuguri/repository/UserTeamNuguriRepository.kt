package com.duckvis.nuguri.repository

import com.duckvis.core.domain.nuguri.QUserTeam.userTeam
import com.duckvis.core.domain.nuguri.UserTeam
import com.duckvis.core.types.nuguri.UserTeamLevel
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component

@Component
class UserTeamNuguriRepository(
  private val queryFactory: JPAQueryFactory,
) {

  fun isAnyTeamManager(userId: Long): Boolean {
    val userTeams = queryFactory
      .select(userTeam)
      .from(userTeam)
      .where(
        userTeam.userId.eq(userId)
      )
      .fetch()

    return userTeams.any { userTeam -> userTeam.isManager }
  }

  fun managerUserTeam(userId: Long): UserTeam? {
    val managerUserTeams = queryFactory
      .select(userTeam)
      .from(userTeam)
      .where(
        userTeam.userId.eq(userId),
        userTeam.level.eq(UserTeamLevel.MANAGER)
      )
      .fetch()

    return managerUserTeams.firstOrNull()
  }

}