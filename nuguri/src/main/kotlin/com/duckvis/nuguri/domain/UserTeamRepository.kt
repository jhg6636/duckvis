package com.duckvis.nuguri.domain

import com.duckvis.nuguri.domain.UserTeam
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository

@Repository
@EnableJpaRepositories
interface UserTeamRepository: JpaRepository<UserTeam, Long> {
    fun findAllByTeamId(teamId: Long): List<UserTeam>
}