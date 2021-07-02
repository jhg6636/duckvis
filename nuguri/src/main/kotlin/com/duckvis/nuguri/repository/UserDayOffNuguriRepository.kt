package com.duckvis.nuguri.repository

import com.duckvis.core.domain.nuguri.QUserDayOff.userDayOff
import com.duckvis.core.types.nuguri.DayOffType
import com.querydsl.core.Tuple
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class UserDayOffNuguriRepository(
  private val queryFactory: JPAQueryFactory,
) {

  fun getTotalDayOffBetween(from: LocalDateTime, to: LocalDateTime): List<Tuple> {
    return queryFactory
      .select(userDayOff.userId, userDayOff.days.sum())
      .from(userDayOff)
      .where(
        userDayOff.createdDateTime.between(from, to),
        userDayOff.type.eq(DayOffType.NORMAL),
      )
      .groupBy(userDayOff.userId)
      .fetch()
  }

  fun getTotalSickDayOffBetween(from: LocalDateTime, to: LocalDateTime): List<Tuple> {
    return queryFactory
      .select(userDayOff.userId, userDayOff.days.sum())
      .from(userDayOff)
      .where(
        userDayOff.createdDateTime.between(from, to),
        userDayOff.type.eq(DayOffType.SICK),
      )
      .groupBy(userDayOff.userId)
      .fetch()
  }

  fun getMyTotalDayOffBetween(userId: Long, type: DayOffType, from: LocalDateTime, to: LocalDateTime): Int {
    return queryFactory
      .select(userDayOff.days.sum())
      .from(userDayOff)
      .where(
        userDayOff.userId.eq(userId),
        userDayOff.type.eq(type),
        userDayOff.createdDateTime.between(from, to)
      )
      .groupBy(userDayOff.userId)
      .fetchOne() ?: 0
  }

}