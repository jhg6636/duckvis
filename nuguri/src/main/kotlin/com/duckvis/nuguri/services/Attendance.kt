package com.duckvis.nuguri.services

import com.duckvis.nuguri.domain.AttendanceCard
import com.duckvis.nuguri.dtos.WorkTypeDto

interface Attendance {
    fun responseLogin(
        userId: Long,
        projectId: Long,
        workTypeDto: WorkTypeDto = WorkTypeDto()
    ): AttendanceCard

    fun responseLogout(userId: Long): AttendanceCard
    fun responseMistake(
        userId: Long,
        projectId: Long,
        durationMinutes: Int,
        workTypeDto: WorkTypeDto = WorkTypeDto()
    ): AttendanceCard

    fun startCoreTime(): List<Long> // userId list
}