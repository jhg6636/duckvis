package com.catshi.nuguri.domain

import com.catshi.core.utils.TimeHandler
import org.springframework.transaction.annotation.Transactional
import java.time.*
import javax.persistence.*

@Entity
class AttendanceCard(
    val userId: Long,
    val projectId: Long,

    @Enumerated(EnumType.STRING)
    val type: AttendanceType,
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

//    fun logOut() {
//        saveLogoutTime(TimeHandler.nowDateTime())
//        durationCalculate()
//    }

    fun durationCalculate() {
        this.durationSeconds = (this.logoutDateTime!!.second - this.loginDateTime.second)
        + (this.logoutDateTime!!.minute - this.loginDateTime.minute) * 60
        + (this.logoutDateTime!!.hour - this.loginDateTime.hour) * 3600
    }
}