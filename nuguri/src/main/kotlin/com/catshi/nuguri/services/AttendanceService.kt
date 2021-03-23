package com.catshi.nuguri.services

import com.catshi.core.types.UserLevelType
import com.catshi.core.utils.TimeHandler
import com.catshi.nuguri.domain.AttendanceCard
import com.catshi.nuguri.domain.AttendanceCardQueryDslRepository
import com.catshi.nuguri.domain.AttendanceCardRepository
import com.catshi.nuguri.domain.CardType
import com.catshi.nuguri.dtos.HowLongIWorkedResponse
import com.catshi.nuguri.exceptions.AlreadyAttendedException
import com.catshi.nuguri.exceptions.NotWorkingException
import com.catshi.nuguri.types.AttendanceOption
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class AttendanceService(
    private val attendanceCardQueryDslRepository: AttendanceCardQueryDslRepository,
    private val attendanceCardRepository: AttendanceCardRepository,
) : Attendance {
    @Transactional
    override fun responseLogin(userId: Long, projectId: Long, attendanceOption: AttendanceOption): AttendanceCard {
        checkAttendedAllSamely(userId, projectId, attendanceOption)
        return saveCard(userId, projectId, attendanceOption, CardType.WORK)
    }

    private fun checkAttendedAllSamely(userId: Long, projectId: Long, attendanceOption: AttendanceOption) {
        val nowWorkingCard = attendanceCardQueryDslRepository.getMyWorkingCard(userId)
        if (nowWorkingCard != null &&
            nowWorkingCard.isSameProjectId(projectId) &&
            nowWorkingCard.isSameAttendanceOption(attendanceOption)
        ) {
            throw AlreadyAttendedException()
        }
    }

    private fun saveCard(
        userId: Long,
        projectId: Long,
        attendanceOption: AttendanceOption,
        cardType: CardType
    ): AttendanceCard {
        attendanceCardQueryDslRepository.getMyWorkingCard(userId)?.logOut()
        val newCard = AttendanceCard(userId, projectId, cardType, attendanceOption, TimeHandler.nowDateTime())
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
        attendanceOption: AttendanceOption
    ): AttendanceCard {
        return attendanceCardRepository.save(
            AttendanceCard(
                userId,
                projectId,
                CardType.MISTAKE,
                attendanceOption,
                TimeHandler.nowDateTime(),
                durationMinutes * 60
            )
        )
    }

    override fun startCoreTime(): List<Long> {
        TODO("Not yet implemented")
    }

    override fun endCoreTime() {
        TODO("Not yet implemented")
    }
}