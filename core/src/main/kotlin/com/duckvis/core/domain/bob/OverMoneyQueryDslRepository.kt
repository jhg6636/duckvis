package com.duckvis.core.domain.bob

import com.duckvis.core.domain.bob.QOverMoney.overMoney
import com.duckvis.core.types.bob.BobTimeType
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Component
class OverMoneyQueryDslRepository(
  private val queryFactory: JPAQueryFactory,
) {
  fun thisMealOverMoney(userId: Long, date: LocalDate, bobTimeType: BobTimeType): OverMoney? {
    return queryFactory
      .select(overMoney)
      .from(overMoney)
      .where(
        overMoney.userId.eq(userId),
        overMoney.dateTime.between(
          LocalDateTime.of(date.minusDays(1), LocalTime.of(15, 0)),
          LocalDateTime.of(date, LocalTime.of(15, 0))
        ),
        overMoney.bobTimeType.eq(bobTimeType)
      )
      .fetchOne()
  }
}