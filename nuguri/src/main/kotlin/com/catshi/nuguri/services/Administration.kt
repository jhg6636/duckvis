package com.catshi.nuguri.services

import com.catshi.core.domain.Team
import com.catshi.core.types.UserLevelType
import com.catshi.nuguri.domain.Project

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