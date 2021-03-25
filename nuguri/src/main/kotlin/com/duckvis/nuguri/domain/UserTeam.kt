package com.duckvis.nuguri.domain

import com.duckvis.nuguri.types.UserTeamLevel
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class UserTeam (
    val userId: Long,
    private val teamId: Long,
    private var level: UserTeamLevel = UserTeamLevel.MEMBER,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}