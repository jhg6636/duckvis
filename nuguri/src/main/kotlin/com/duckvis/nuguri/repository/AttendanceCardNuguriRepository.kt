package com.duckvis.nuguri.repository

import com.duckvis.core.domain.nuguri.AttendanceCard
import com.duckvis.core.domain.nuguri.QAttendanceCard.attendanceCard
import com.duckvis.core.types.nuguri.CardType
import com.duckvis.core.utils.coreTimeEnd
import com.duckvis.core.utils.coreTimeStart
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class AttendanceCardNuguriRepository(
  private val queryFactory: JPAQueryFactory
) {

  fun getMyWorkingCard(userId: Long): AttendanceCard? {
    return queryFactory
      .select(attendanceCard)
      .from(attendanceCard)
      .where(
        attendanceCard.userId.eq(userId),
        attendanceCard.logoutDateTime.isNull,
        attendanceCard.type.eq(CardType.WORK)
      )
      .fetchOne()
  }

  fun getMyCardsBetween(userId: Long, startTime: LocalDateTime, endTime: LocalDateTime): List<AttendanceCard> {
    return queryFactory
      .select(attendanceCard)
      .from(attendanceCard)
      .where(
        attendanceCard.userId.eq(userId),
        attendanceCard.loginDateTime.between(startTime, endTime)
      )
      .fetch()
  }

  fun getAllCardsBetween(startTime: LocalDateTime, endTime: LocalDateTime): List<AttendanceCard> {
    return queryFactory
      .select(attendanceCard)
      .from(attendanceCard)
      .where(
        attendanceCard.loginDateTime.between(startTime, endTime)
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

  fun getAttendedAt(at: LocalDateTime): List<AttendanceCard> {
    return queryFactory
      .select(attendanceCard)
      .from(attendanceCard)
      .where(
        attendanceCard.type.eq(CardType.WORK),
        attendanceCard.loginDateTime.before(at),
        attendanceCard.logoutDateTime.after(at).or(attendanceCard.logoutDateTime.isNull)
      )
      .fetch()
  }

  fun getCoreTimeCards(date: LocalDate): List<AttendanceCard> {
    return queryFactory
      .select(attendanceCard)
      .from(attendanceCard)
      .where(
        attendanceCard.type.eq(CardType.WORK),
        attendanceCard.loginDateTime.before(date.coreTimeStart),
        attendanceCard.logoutDateTime.after(date.coreTimeStart).or(attendanceCard.logoutDateTime.isNull)
      )
      .fetch().plus(
        queryFactory
          .select(attendanceCard)
          .from(attendanceCard)
          .where(
            attendanceCard.type.eq(CardType.WORK),
            attendanceCard.loginDateTime.between(date.coreTimeStart, date.coreTimeEnd)
          )
          .fetch()
      )
  }

}