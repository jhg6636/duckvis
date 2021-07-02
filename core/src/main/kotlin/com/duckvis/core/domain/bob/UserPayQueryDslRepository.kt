package com.duckvis.core.domain.bob

import com.duckvis.core.domain.bob.QUserPay.userPay
import com.duckvis.core.types.bob.BobTimeType
import com.duckvis.core.utils.DateTimeMaker
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Component
class UserPayQueryDslRepository(
  private val queryFactory: JPAQueryFactory,
) {
  fun thisMealUserPay(userId: Long, bobTimeType: BobTimeType): UserPay? {
    return queryFactory
      .select(userPay)
      .from(userPay)
      .where(
        userPay.bobTimeType.eq(bobTimeType),
        isToday,
        userPay.userId.eq(userId)
      )
      .fetchOne()
  }

  fun getPayToModify(userId: Long, date: LocalDate, bobTimeType: BobTimeType): UserPay? {
    return queryFactory
      .select(userPay)
      .from(userPay)
      .where(
        userPay.bobTimeType.eq(bobTimeType),
        userPay.dateTime.between(
          LocalDateTime.of(date.minusDays(1), LocalTime.of(21, 0)),
          LocalDateTime.of(date, LocalTime.of(21, 0))
        ),
        userPay.userId.eq(userId)
      )
      .fetchOne()
  }

  fun hasTodayPay(userId: Long, date: LocalDate): Boolean {
    return queryFactory
      .select(userPay)
      .from(userPay)
      .where(
        userPay.dateTime.between(
          LocalDateTime.of(date.minusDays(1), LocalTime.of(21, 0)),
          LocalDateTime.of(date, LocalTime.of(21, 0))
        ),
        userPay.userId.eq(userId)
      )
      .fetchOne() != null
  }

  fun getMyRecentPays(userId: Long, size: Int = 2): List<UserPay> {
    return queryFactory
      .select(userPay)
      .from(userPay)
      .where(
        userPay.userId.eq(userId),
      )
      .orderBy(userPay.dateTime.desc())
      .fetch()
      .subList(0, size)
  }

  private val isToday: BooleanExpression
    get() {
      return (userPay.dateTime.dayOfMonth().eq(DateTimeMaker.nowDateTime().dayOfMonth)
        .and(userPay.dateTime.month().eq(DateTimeMaker.nowDateTime().monthValue)))
        .and(userPay.dateTime.year().eq(DateTimeMaker.nowDateTime().year))
    }
}