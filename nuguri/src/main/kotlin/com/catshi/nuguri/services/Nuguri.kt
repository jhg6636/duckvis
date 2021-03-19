package com.catshi.nuguri.services

import com.catshi.nuguri.domain.AttendanceCard
import com.catshi.nuguri.domain.Project
import com.catshi.core.domain.Team
import com.catshi.core.types.UserLevelType
import com.catshi.core.utils.TimeHandler
import com.catshi.nuguri.dtos.HowLongIWorkedResponse
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.ZoneId

@Service
interface Nuguri {
    fun responseLogin(userId: Long, projectId: Long): AttendanceCard
    fun responseLogout(userId: Long): AttendanceCard
    fun responseMistake(userId: Long, projectId: Long, durationMinutes: Int): AttendanceCard
    fun responseStatistics(
        userId: Long, startDate: LocalDate = TimeHandler.nowDate(), endDate: LocalDate = TimeHandler.nowDate()
    ): Int
    fun responseAdminStatistics(): List<AttendanceCard>

    fun responseNow(): List<AttendanceCard>
    fun responseHowLongIWorked(userId: Long): HowLongIWorkedResponse
    fun responseShowAllProjects(): List<Project>

    fun responseAddProject(name: String, nickname: String, teamId: Long): Project
    fun responseDeleteProject(projectId: Long)
    fun responseAddTeam(name: String, managerId: Long): Team
    fun responseDeleteTeam(name: String)
    fun responseAddTeamMember(userId: Long, teamId: Long)
    fun responseDeleteTeamMember(userId: Long, teamId: Long)

    fun responseHelp(userLevel: UserLevelType): String

    fun startCoreTime(): List<Long> // userId list // or return String
    fun endCoreTime()
}