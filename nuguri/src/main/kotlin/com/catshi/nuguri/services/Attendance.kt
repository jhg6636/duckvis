package com.catshi.nuguri.services

import com.catshi.core.types.UserLevelType
import com.catshi.nuguri.domain.AttendanceCard
import com.catshi.nuguri.dtos.HowLongIWorkedResponse
import com.catshi.nuguri.types.AttendanceOption

interface Attendance {
    fun responseLogin(
        userId: Long,
        projectId: Long,
        attendanceOption: AttendanceOption = AttendanceOption.NORMAL
    ): AttendanceCard

    fun responseLogout(userId: Long): AttendanceCard
    fun responseMistake(
        userId: Long,
        projectId: Long,
        durationMinutes: Int,
        attendanceOption: AttendanceOption = AttendanceOption.NORMAL
    ): AttendanceCard

    fun startCoreTime(): List<Long> // userId list
    fun endCoreTime()
}