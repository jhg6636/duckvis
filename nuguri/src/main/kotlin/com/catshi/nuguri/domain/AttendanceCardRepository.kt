package com.catshi.nuguri.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime

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