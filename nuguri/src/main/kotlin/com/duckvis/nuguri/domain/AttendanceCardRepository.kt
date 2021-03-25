package com.duckvis.nuguri.domain

import com.duckvis.nuguri.domain.AttendanceCard
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository

@Repository
@EnableJpaRepositories
interface AttendanceCardRepository : JpaRepository<AttendanceCard, Long> {
//    fun findAttendanceCardByUserIdAndLogoutDateTimeIsNull(userId: Long): AttendanceCard?
//    fun findAttendanceCardsByUserIdAndLogoutDateTimeBetween(
//        userId: Long,
//        startDateTime: LocalDateTime,
//        endDateTime: LocalDateTime
//    ): List<AttendanceCard>
//
//    fun findAttendanceCardsByTypeAndLogoutDateTimeIsNull(type: AttendanceType): List<AttendanceCard>
}