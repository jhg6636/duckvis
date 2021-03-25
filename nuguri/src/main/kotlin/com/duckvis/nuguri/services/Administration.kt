package com.duckvis.nuguri.services

import com.duckvis.core.types.UserLevelType
import com.duckvis.nuguri.domain.Project
import com.duckvis.nuguri.domain.Team

interface Administration {
    fun responseShowAllProjects(): List<Project>

    fun responseAddProject(name: String, nickname: String, teamId: Long): Project
    fun responseDeleteProject(projectId: Long)
    fun responseAddTeam(name: String, managerId: Long): Team
    fun responseDeleteTeam(name: String)
    fun responseAddTeamMember(userId: Long, teamId: Long)
    fun responseDeleteTeamMember(userId: Long, teamId: Long)

    fun responseHelp(userLevel: UserLevelType): String
}