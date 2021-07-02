package com.duckvis.core.domain.nuguri

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository

@Repository
@EnableJpaRepositories
interface UserTeamRepository : JpaRepository<UserTeam, Long> {
  fun findAllByTeamId(teamId: Long): List<UserTeam>
  fun findAllByUserId(userId: Long): List<UserTeam>
  fun findByUserIdAndTeamId(userId: Long, teamId: Long): UserTeam?
}