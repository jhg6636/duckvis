package com.duckvis.core.domain.bob

import com.duckvis.core.domain.bob.QBobHistory.bobHistory
import com.duckvis.core.domain.bob.QBobTicket.bobTicket
import com.duckvis.core.exceptions.bob.NoBobTicketException
import com.duckvis.core.types.bob.BobTimeType
import com.duckvis.core.types.bob.PayType
import com.duckvis.core.utils.DateTimeMaker
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Component
class BobHistoryQueryDslRepository(
  private val queryFactory: JPAQueryFactory,
) {

  @Transactional
  fun bobLeaderCount(userId: Long): Long {
    return queryFactory
      .select(bobHistory)
      .from(bobHistory)
      .leftJoin(bobTicket)
      .on(bobHistory.bobTicketId.eq(bobTicket.id))
      .where(
        bobHistory.isBobLeader.eq(true),
        bobHistory.userId.eq(userId),
        isThisMonth
      )
      .fetchCount()
  }

  @Transactional
  fun teammates(userId: Long): List<Long> {
    val eatDates = queryFactory
      .select(bobHistory)
      .from(bobHistory)
      .leftJoin(bobTicket)
      .on(bobHistory.bobTicketId.eq(bobTicket.id))
      .where(
        bobHistory.userId.eq(userId),
        isThisMonth
      )
      .fetch()
    val returnList = mutableListOf<Long>()
    eatDates.forEach { thisBobHistory ->
      val thisBobTicket = queryFactory
        .select(bobTicket)
        .from(bobTicket)
        .where(bobTicket.id.eq(thisBobHistory.bobTicketId))
        .fetchOne() ?: throw NoBobTicketException()
      val teammates = queryFactory
        .select(bobHistory)
        .from(bobHistory)
        .leftJoin(bobTicket)
        .on(
          bobHistory.bobTicketId.eq(bobTicket.id)
        )
        .where(
          bobHistory.bobTeamNumber.eq(thisBobHistory.bobTeamNumber),
          bobTicket.bobTimeType.eq(thisBobTicket.bobTimeType),
          bobTicket.date.eq(thisBobTicket.date),
          bobTicket.userId.ne(userId)
        )
        .fetch()
      returnList.addAll(teammates.map { bobHistory -> bobHistory.userId })
    }

    return returnList
  }

  @Transactional
  fun countThisMealHistory(bobTimeType: BobTimeType): Long {
    return queryFactory
      .select(bobHistory)
      .from(bobHistory)
      .leftJoin(bobTicket)
      .on(bobHistory.bobTicketId.eq(bobTicket.id))
      .where(
        bobTicket.date.eq(DateTimeMaker.nowDate()),
        bobTicket.bobTimeType.eq(bobTimeType)
      ).fetchCount()
  }

  @Transactional
  fun getThisMealUserIds(bobTimeType: BobTimeType): List<Long> {
    return queryFactory
      .select(bobHistory.userId)
      .from(bobHistory)
      .leftJoin(bobTicket)
      .on(bobHistory.bobTicketId.eq(bobTicket.id))
      .where(
        bobTicket.date.eq(DateTimeMaker.nowDate()),
        bobTicket.bobTimeType.eq(bobTimeType)
      )
      .fetch()
  }

  @Transactional
  fun getThisMealPayType(userId: Long, date: LocalDate, bobTimeType: BobTimeType): PayType? {
    return queryFactory
      .select(bobHistory.payType)
      .from(bobHistory)
      .innerJoin(bobTicket)
      .on(bobHistory.bobTicketId.eq(bobTicket.id))
      .where(
        bobHistory.userId.eq(userId),
        bobTicket.date.eq(date),
        bobTicket.bobTimeType.eq(bobTimeType)
      )
      .fetchOne()
  }

  fun bobTeamNumbers(userId: Long, from: LocalDate, to: LocalDate): List<Int> {
    return queryFactory
      .select(bobHistory.bobTeamNumber)
      .from(bobHistory)
      .where(
        bobHistory.userId.eq(userId),
        bobHistory.createdDateTime.between(LocalDateTime.of(from, LocalTime.MIDNIGHT), LocalDateTime.of(to, LocalTime.of(23, 59, 59)))
      )
      .fetch()
  }

  private val isThisMonth: BooleanExpression
    get() = bobTicket.date.between(
      DateTimeMaker.nowDate().withDayOfMonth(1),
      DateTimeMaker.nowDate().withDayOfMonth(DateTimeMaker.nowDate().lengthOfMonth())
    )

}
