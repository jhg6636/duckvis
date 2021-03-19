package com.catshi.nuguri.services

import com.catshi.core.domain.Team
import com.catshi.core.domain.TeamRepository
import com.catshi.core.domain.UserRepository
import com.catshi.core.exceptions.NoSuchTeamException
import com.catshi.core.exceptions.NotTeamMemberException
import com.catshi.core.exceptions.TeamMemberAlreadyExistsException
import com.catshi.core.exceptions.TeamNameAlreadyExistsException
import com.catshi.core.types.UserLevelType
import com.catshi.core.utils.TimeHandler
import com.catshi.nuguri.domain.*
import com.catshi.nuguri.dtos.HowLongIWorkedResponse
import com.catshi.nuguri.exceptions.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

@Service
open class NuguriService(
    private val attendanceCardRepository: AttendanceCardRepository,
    private val attendanceCardQueryDslRepository: AttendanceCardQueryDslRepository,
    private val projectRepository: ProjectRepository,
    private val teamRepository: TeamRepository,
    private val userRepository: UserRepository
) : Nuguri {
    @Transactional
    override fun responseLogin(userId: Long, projectId: Long): AttendanceCard {
        val nowWorkingCard = getNowWorkingCard(userId)
        if (nowWorkingCard?.projectId == projectId) throw AlreadyLoggedInProjectException()
        else if (nowWorkingCard != null) logOut(nowWorkingCard)
        return createAttendanceCard(userId, projectId)
    }

    private fun createAttendanceCard(userId: Long, projectId: Long): AttendanceCard {
        return attendanceCardRepository.save(
            AttendanceCard(
                userId,
                projectId,
                AttendanceType.WORK,
                TimeHandler.nowDateTime()
            )
        )
    }

    @Transactional
    override fun responseLogout(userId: Long): AttendanceCard {
        val attendanceCard = getNowWorkingCard(userId) ?: throw NotWorkingException()
        return logOut(attendanceCard)
    }

    @Transactional
    fun logOut(attendanceCard: AttendanceCard): AttendanceCard {
        attendanceCard.saveLogoutTime(TimeHandler.nowDateTime())
        attendanceCard.durationCalculate()
        val savedCard = attendanceCardRepository.save(attendanceCard)
        println("logOut durationSeconds")
        println(savedCard.durationSeconds)
        return savedCard
    }

    private fun getNowWorkingCard(userId: Long): AttendanceCard? {
        return attendanceCardQueryDslRepository.getMyWorkingCard(userId)
    }

    @Transactional
    override fun responseMistake(userId: Long, projectId: Long, durationMinutes: Int): AttendanceCard {
        return createMistakeCard(userId, projectId, durationMinutes)
    }

    private fun createMistakeCard(userId: Long, projectId: Long, durationMinutes: Int): AttendanceCard {
        return attendanceCardRepository.save(
            AttendanceCard(
                userId,
                projectId,
                AttendanceType.MISTAKE,
                LocalDateTime.now(ZoneId.of("Asia/Seoul")),
                durationMinutes * 60
            )
        )
    }

    @Transactional
    override fun responseStatistics(userId: Long, startDate: LocalDate, endDate: LocalDate): Int {
        return getTotalDurationSecondsBetweenDates(userId, startDate, endDate)
    }

    @Transactional
    override fun responseAdminStatistics(): List<AttendanceCard> {
        return emptyList()
    }

    private fun getTotalDurationSecondsBetweenDates(userId: Long, startDate: LocalDate, endDate: LocalDate): Int {
        val startDateTime = LocalDateTime.of(startDate.year, startDate.month, startDate.dayOfMonth, 0, 0, 0)
        val endDateTime = LocalDateTime.of(endDate.year, endDate.month, endDate.dayOfMonth, 23, 59, 59)
        return attendanceCardQueryDslRepository.getMyAttendanceCardsBetween(userId, startDateTime, endDateTime)
            .sumBy { it.durationSeconds ?: throw InvalidAttendanceCardException() }
    }

    @Transactional
    override fun responseNow(): List<AttendanceCard> {
        return attendanceCardQueryDslRepository.getNowWorkingCards()
    }

    @Transactional
    override fun responseHowLongIWorked(userId: Long): HowLongIWorkedResponse {
        return HowLongIWorkedResponse(getTodayWorkingSeconds(userId), getThisWeekWorkingSeconds(userId))
    }

    private fun getTodayWorkingSeconds(userId: Long): Int {
        return getTotalDurationSecondsBetweenDates(
            userId,
            TimeHandler.nowDate(),
            TimeHandler.nowDate()
        )
    }

    private fun getThisWeekWorkingSeconds(userId: Long): Int {
        return getTotalDurationSecondsBetweenDates(
            userId,
            TimeHandler.nowDate()
                .minusDays(TimeHandler.nowDate().dayOfWeek.value.toLong() - 1),
            TimeHandler.nowDate()
                .plusDays(7 - TimeHandler.nowDate().dayOfWeek.value.toLong())
        )
    }

    @Transactional
    override fun responseShowAllProjects(): List<Project> {
        return projectRepository.findAll()
    }

    @Transactional
    override fun responseAddProject(name: String, nickname: String, teamId: Long): Project {
        if (projectRepository.existsByName(name)) throw ProjectNameAlreadyExistsException()
        if (projectRepository.existsByNickname(nickname)) throw ProjectNicknameAlreadyExistsException()
        return projectRepository.save(Project(name, nickname, teamId))
    }

    @Transactional
    override fun responseDeleteProject(projectId: Long) {
        val project = projectRepository.findByIdOrNull(projectId) ?: throw NoSuchProjectException()
        projectRepository.delete(project)
    }

    @Transactional
    override fun responseAddTeam(name: String, managerId: Long): Team {
        if (teamRepository.existsByName(name)) throw TeamNameAlreadyExistsException()
        return teamRepository.save(Team(name, managerId))
    }

    @Transactional
    override fun responseDeleteTeam(name: String) {
        val team = teamRepository?.findByName(name) ?: throw NoSuchTeamException()
        teamRepository.delete(team)
    }

    @Transactional
    override fun responseAddTeamMember(userId: Long, teamId: Long) {
        val team = teamRepository.findByIdOrNull(teamId) ?: throw NoSuchTeamException()
        userRepository.findByIdOrNull(userId)?.apply {
            if (this.teamId == teamId) throw TeamMemberAlreadyExistsException()
            this.teamId = team.id
        } ?: throw NoSuchUserException()
    }

    @Transactional
    override fun responseDeleteTeamMember(userId: Long, teamId: Long) {
        teamRepository.findByIdOrNull(teamId) ?: throw NoSuchTeamException()
        userRepository.findByIdOrNull(userId)?.apply {
            if (this.teamId != teamId) throw NotTeamMemberException()
            this.teamId = null
        } ?: throw NoSuchUserException()
    }

    @Transactional
    override fun startCoreTime(): List<Long> {
        val time = TimeHandler.nowDateTime()
        return getNotWorkingOnCoreTime()
//        return if (time.hour == 13 && time.minute == 0 && time.second == 0 && time.dayOfWeek < DayOfWeek.SATURDAY) {
//            try {
//                getNotWorkingOnCoreTime()
//            } catch (e: NobodyIsWorkingException) {
//                val allUsers = mutableListOf<Long>()
//                userRepository.findAll().forEach { allUsers.add(it.id) }
//                allUsers
//            }
//        } else listOf()
    }

    override fun responseHelp(userLevel: UserLevelType): String {
        return ""
    }

    private fun getNotWorkingOnCoreTime(): List<Long> {
        val nowWorkingUsers = mutableListOf<Long>()
        attendanceCardQueryDslRepository.getNowWorkingCards()
            .forEach {
                nowWorkingUsers.add(it.userId)
            }
        val allUsers = mutableListOf<Long>()
        userRepository.findAll().forEach { allUsers.add(it.id) }
        return allUsers - nowWorkingUsers
    }

    override fun endCoreTime() {
        println("코어타임 종료")
    }
}