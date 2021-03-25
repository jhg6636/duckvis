package com.duckvis.nuguri.services

import com.duckvis.nuguri.domain.AttendanceCard
import com.duckvis.nuguri.dtos.HowLongIWorkedResponse
import com.duckvis.nuguri.dtos.WorkTypeDto
import java.time.LocalDateTime

interface Statistics {
    fun responseNow(): List<AttendanceCard>
    fun responseHowLongIWorked(userId: Long): HowLongIWorkedResponse
    fun responseStatistics(
        userId: Long,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime? = null,
        workTypeDto: WorkTypeDto = WorkTypeDto()
    ): Int

    fun responseAdminStatistics(
        projectId: Long?,
        teamId: Long?,
        userId: Long?,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime?
    ): Map<Long, Int> // userId, Int

    fun responseLastLogOut(userId: Long): AttendanceCard?
}