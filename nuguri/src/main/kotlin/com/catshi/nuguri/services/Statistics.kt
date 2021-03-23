package com.catshi.nuguri.services

import com.catshi.core.utils.TimeHandler
import com.catshi.nuguri.domain.AttendanceCard
import com.catshi.nuguri.dtos.HowLongIWorkedResponse
import java.time.LocalDate

interface Statistics {
    fun responseNow(): List<AttendanceCard>
    fun responseHowLongIWorked(userId: Long): HowLongIWorkedResponse
    fun responseStatistics(
        userId: Long, startDate: LocalDate = TimeHandler.nowDate(), endDate: LocalDate = TimeHandler.nowDate()
    ): Int
    fun responseAdminStatistics(): List<AttendanceCard>
}