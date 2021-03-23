package com.catshi.nuguri.domain

import com.catshi.nuguri.domain.QAttendanceCard.attendanceCard
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class AttendanceCardQueryDslRepository(
    private val queryFactory: JPAQueryFactory
){
    fun getMyWorkingCard(userId: Long): AttendanceCard? {
        return queryFactory
            .select(attendanceCard)
            .from(attendanceCard)
            .where(
                attendanceCard.userId.eq(userId),
                attendanceCard.logoutDateTime.isNull
            )
            .fetchOne()
    }

    fun getMyAttendanceCardsBetween(userId: Long, startTime: LocalDateTime, endTime: LocalDateTime): List<AttendanceCard> {
        return queryFactory
            .select(attendanceCard)
            .from(attendanceCard)
            .where(
                attendanceCard.userId.eq(userId),
                attendanceCard.logoutDateTime.between(startTime, endTime)
            )
            .fetch()
    }

    fun getAllMistakeCards(): List<AttendanceCard> {
        return queryFactory
            .select(attendanceCard)
            .from(attendanceCard)
            .where(
                attendanceCard.type.eq(CardType.MISTAKE),
                attendanceCard.logoutDateTime.isNull
            )
            .fetch()
    }

    fun getNowWorkingCards(): List<AttendanceCard> {
        return queryFactory
            .select(attendanceCard)
            .from(attendanceCard)
            .where(
                attendanceCard.type.eq(CardType.WORK),
                attendanceCard.logoutDateTime.isNull
            )
            .fetch()
    }
}