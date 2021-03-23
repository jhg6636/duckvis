package com.catshi.nuguri.domain

import com.catshi.core.utils.TimeHandler
import com.catshi.nuguri.types.AttendanceOption
import java.time.*
import javax.persistence.*

@Entity
class AttendanceCard(
    val userId: Long,
    val projectId: Long,

    @Enumerated(EnumType.STRING)
    val type: CardType,

    @Enumerated(EnumType.STRING)
    val option: AttendanceOption,
    private val loginDateTime: LocalDateTime,
    var durationSeconds: Int? = null,
    var logoutDateTime: LocalDateTime? = null
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int = -1

    val createdDateTime: LocalDateTime = TimeHandler.nowDateTime()
    fun saveLogoutTime(logoutDateTime: LocalDateTime) {
        this.logoutDateTime = logoutDateTime
    }

    fun logOut() {
        saveLogoutTime(TimeHandler.nowDateTime())
        durationCalculate()
    }

    fun durationCalculate() {
        this.durationSeconds = (this.logoutDateTime!!.second - this.loginDateTime.second)
        + (this.logoutDateTime!!.minute - this.loginDateTime.minute) * 60
        + (this.logoutDateTime!!.hour - this.loginDateTime.hour) * 3600
    }

    fun isSameProjectId(projectId: Long): Boolean {
        return this.projectId == projectId
    }

    fun isSameAttendanceOption(attendanceOption: AttendanceOption): Boolean {
        return this.option == attendanceOption
    }
}