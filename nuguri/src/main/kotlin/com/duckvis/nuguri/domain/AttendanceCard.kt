package com.duckvis.nuguri.domain

import com.duckvis.core.utils.TimeHandler
import java.time.*
import javax.persistence.*

@Entity
class AttendanceCard(
    val userId: Long,
    val projectId: Long,
    @Enumerated(EnumType.STRING) val type: CardType,
    val loginDateTime: LocalDateTime,
    var durationSeconds: Int? = null,
    var logoutDateTime: LocalDateTime? = null,
    val isNight: Boolean = false,
    val isExtended: Boolean = false,
    val isHoliday: Boolean = false,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int = -1

    fun logOut() {
        val now = TimeHandler.nowDateTime()
        this.logoutDateTime = now
        val duration = Duration.between(this.loginDateTime, now)
        this.durationSeconds = duration.toSeconds().toInt()
    }

    fun isSameProjectId(projectId: Long): Boolean {
        return this.projectId == projectId
    }

    val isNowWorking: Boolean
        get() = this.type == CardType.WORK && this.durationSeconds == null && this.logoutDateTime == null
}