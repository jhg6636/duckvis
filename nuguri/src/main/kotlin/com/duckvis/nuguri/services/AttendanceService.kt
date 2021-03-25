package com.duckvis.nuguri.services

import com.duckvis.core.domain.UserRepository
import com.duckvis.core.utils.TimeHandler
import com.duckvis.nuguri.domain.AttendanceCard
import com.duckvis.nuguri.domain.AttendanceCardQueryDslRepository
import com.duckvis.nuguri.domain.AttendanceCardRepository
import com.duckvis.nuguri.domain.CardType
import com.duckvis.nuguri.dtos.WorkTypeDto
import com.duckvis.nuguri.exceptions.AlreadyAttendedException
import com.duckvis.nuguri.exceptions.NotWorkingException
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class AttendanceService(
    private val attendanceCardQueryDslRepository: AttendanceCardQueryDslRepository,
    private val attendanceCardRepository: AttendanceCardRepository,
    private val userRepository: UserRepository,
) : Attendance {
    @Transactional
    override fun responseLogin(userId: Long, projectId: Long, workTypeDto: WorkTypeDto): AttendanceCard {
        val nowWorkingCard = checkNowWorkingCard(userId, projectId, workTypeDto)
        nowWorkingCard?.logOut()
        return saveCard(userId, projectId, workTypeDto)
    }

    private fun checkNowWorkingCard(userId: Long, projectId: Long, workTypeDto: WorkTypeDto): AttendanceCard? {
        val nowWorkingCard = attendanceCardQueryDslRepository.getMyWorkingCard(userId)
        if (nowWorkingCard != null &&
            nowWorkingCard.isSameProjectId(projectId) &&
            workTypeDto == WorkTypeDto(nowWorkingCard.isNight, nowWorkingCard.isExtended, nowWorkingCard.isHoliday)
        ) {
            throw AlreadyAttendedException()
        }
        return nowWorkingCard
    }

    private fun saveCard(userId: Long, projectId: Long, workTypeDto: WorkTypeDto): AttendanceCard {
        attendanceCardQueryDslRepository.getMyWorkingCard(userId)?.logOut()
        val newCard = AttendanceCard(
            userId,
            projectId,
            CardType.WORK,
            TimeHandler.nowDateTime(),
            isNight = workTypeDto.isNight,
            isExtended = workTypeDto.isExtended,
            isHoliday = workTypeDto.isHoliday
        )
        return attendanceCardRepository.save(newCard)
    }

    override fun responseLogout(userId: Long): AttendanceCard {
        val nowWorkingCard = attendanceCardQueryDslRepository.getMyWorkingCard(userId)
        nowWorkingCard?.logOut() ?: throw NotWorkingException()
        return nowWorkingCard
    }

    override fun responseMistake(
        userId: Long,
        projectId: Long,
        durationMinutes: Int,
        workTypeDto: WorkTypeDto
    ): AttendanceCard {
        return attendanceCardRepository.save(
            AttendanceCard(
                userId,
                projectId,
                CardType.MISTAKE,
                TimeHandler.nowDateTime(),
                durationMinutes * 60,
                null,
                workTypeDto.isNight,
                workTypeDto.isExtended,
                workTypeDto.isHoliday
            )
        )
    }

    override fun startCoreTime(): List<Long> {
        val nowWorkingUserIds = attendanceCardQueryDslRepository.getNowWorkingCards().map { it.userId }

        return userRepository.findAll().map { it.id }.filter { !nowWorkingUserIds.contains(it) }
    }
}