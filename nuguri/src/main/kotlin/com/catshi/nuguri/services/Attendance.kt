package com.catshi.nuguri.services

import com.catshi.core.types.UserLevelType
import com.catshi.nuguri.domain.AttendanceCard
import com.catshi.nuguri.dtos.HowLongIWorkedResponse

interface Attendance {
    fun responseLogin(userId: Long, projectId: Long): AttendanceCard
    fun responseLogout(userId: Long): AttendanceCard
    fun responseMistake(userId: Long, projectId: Long, durationMinutes: Int): AttendanceCard
    fun responseNow(): List<AttendanceCard>
    fun responseHowLongIWorked(userId: Long): HowLongIWorkedResponse
    fun responseHelp(userLevel: UserLevelType): String

    fun startCoreTime(): List<Long> // userId list
    fun endCoreTime()
}