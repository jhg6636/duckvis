package com.duckvis.nuguri.services

import com.duckvis.core.utils.TimeHandler
import com.duckvis.nuguri.domain.AttendanceCard
import com.duckvis.nuguri.domain.AttendanceCardQueryDslRepository
import com.duckvis.nuguri.domain.UserTeamRepository
import com.duckvis.nuguri.dtos.HowLongIWorkedResponse
import com.duckvis.nuguri.dtos.WorkTypeDto
import com.duckvis.nuguri.exceptions.NotWorkingException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime

@Service
class StatisticsService(
    @Autowired private val attendanceCardQueryDslRepository: AttendanceCardQueryDslRepository,
    @Autowired private val userTeamRepository: UserTeamRepository,
) : Statistics {
    override fun responseNow(): List<AttendanceCard> {
        return attendanceCardQueryDslRepository.getNowWorkingCards()
    }

    override fun responseHowLongIWorked(userId: Long): HowLongIWorkedResponse {
        val nowWorkingCard = attendanceCardQueryDslRepository.getMyWorkingCard(userId) ?: throw NotWorkingException()
        val nowWorkingSeconds =
            Duration.between(nowWorkingCard.loginDateTime, TimeHandler.nowDateTime()).toSeconds().toInt()
        val todayWorkingCards = attendanceCardQueryDslRepository.getMyCardsBetween(
            userId,
            TimeHandler.nowDateTime(),
            TimeHandler.nowDateTime()
        )

        return HowLongIWorkedResponse(
            nowWorkingSeconds,
            todayWorkingCards.filter { !it.isNowWorking }.sumBy { it.durationSeconds ?: 0 } + nowWorkingSeconds
        )
    }

    override fun responseStatistics(
        userId: Long,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime?,
        workTypeDto: WorkTypeDto
    ): Int {
        val allMyCards = attendanceCardQueryDslRepository.getMyCardsBetween(
            userId,
            TimeHandler.dayStartTime(startDateTime),
            endDateTime ?: TimeHandler.dayEndTime(startDateTime)
        ).filter {
            workTypeDto == WorkTypeDto(it.isNight, it.isExtended, it.isHoliday)
        }
        return allMyCards.sumBy { it.durationSeconds ?: 0 }
    }

    override fun responseAdminStatistics(
        projectId: Long?,
        teamId: Long?,
        userId: Long?,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime?
    ): Map<Long, Int> {
        val teamMembers: MutableList<Long> = mutableListOf()
        if (teamId != null) {
            teamMembers.addAll(userTeamRepository.findAllByTeamId(teamId).map { it.userId })
        }
        val allCards = attendanceCardQueryDslRepository.getAllCardsBetween(
            TimeHandler.dayStartTime(startDateTime),
            endDateTime ?: TimeHandler.dayEndTime(startDateTime)
        )
            .filter { it.projectId == projectId || projectId == null }
            .filter { it.userId == userId || userId == null }
            .filter { teamMembers.contains(it.userId) || teamId == null }

        return sortCards(allCards)
    }

    private fun sortCards(cards: List<AttendanceCard>): Map<Long, Int> {
        val map: MutableMap<Long, Int> = mutableMapOf()
        cards.forEach {
            val addingTime = it.durationSeconds ?: 0
            map[it.userId] = map[it.userId]?.plus(addingTime) ?: addingTime
        }

        return map
    }

    override fun responseLastLogOut(userId: Long): AttendanceCard? {
        return attendanceCardQueryDslRepository.getMyCardsBetween(
            userId,
            TimeHandler.nowDateTime().minusDays(15),
            TimeHandler.dayEndTime()
        )
            .filter { it.logoutDateTime != null }
            .maxByOrNull { it.logoutDateTime!! }
    }
}