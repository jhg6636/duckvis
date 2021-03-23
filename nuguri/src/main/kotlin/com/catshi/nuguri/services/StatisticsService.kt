package com.catshi.nuguri.services

import com.catshi.nuguri.domain.AttendanceCard
import com.catshi.nuguri.dtos.HowLongIWorkedResponse
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class StatisticsService: Statistics {
    override fun responseNow(): List<AttendanceCard> {
        TODO("Not yet implemented")
    }

    override fun responseHowLongIWorked(userId: Long): HowLongIWorkedResponse {
        TODO("Not yet implemented")
    }

    override fun responseStatistics(userId: Long, startDate: LocalDate, endDate: LocalDate): Int {
        TODO("Not yet implemented")
    }

    override fun responseAdminStatistics(): List<AttendanceCard> {
        TODO("Not yet implemented")
    }

}