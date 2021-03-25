package com.duckvis.bob.domain

import com.duckvis.bob.domain.QBobHistory.bobHistory
import com.duckvis.bob.types.BobTimeType
import com.duckvis.core.domain.User
import com.duckvis.core.utils.TimeHandler
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class BobHistoryQueryDslRepository(
    private val queryFactory: JPAQueryFactory,
) {
    @Transactional
    fun bobLeaderCount(userId: Long): Long {
        return queryFactory
            .select(bobHistory)
            .from(bobHistory)
            .where(
                bobHistory.isBobLeader.eq(true),
                bobHistory.bobTicket.user.id.eq(userId),
                isThisMonth
            )
            .fetchCount()
    }

    @Transactional
    fun teammates(userId: Long): List<User> {
        val eatDates = queryFactory
            .select(bobHistory)
            .from(bobHistory)
            .where(
                bobHistory.bobTicket.user.id.eq(userId),
                isThisMonth
            )
            .fetch()
        val returnList = mutableListOf<User>()
        eatDates.forEach { it ->
            val teammates = queryFactory
                .select(bobHistory)
                .from(bobHistory)
                .where(
                    bobHistory.bobTeamNumber.eq(it.bobTeamNumber),
                    bobHistory.bobTicket.bobTimeType.eq(it.bobTicket.bobTimeType),
                    bobHistory.bobTicket.date.eq(it.bobTicket.date),
                    bobHistory.bobTicket.user.id.ne(userId)
                )
                .fetch()
            returnList.addAll(teammates.map { it.bobTicket.user })
        }

        return returnList
    }

    @Transactional
    fun countThisMealHistory(bobTimeType: BobTimeType): Long {
        return queryFactory
            .select(bobHistory)
            .from(bobHistory)
            .where(
                bobHistory.bobTicket.date.eq(TimeHandler.nowDate()),
                bobHistory.bobTicket.bobTimeType.eq(bobTimeType)
            ).fetchCount()
    }

    private val isThisMonth: BooleanExpression
        get() = bobHistory.bobTicket.date.between(
            TimeHandler.nowDate().withDayOfMonth(1),
            TimeHandler.nowDate().withDayOfMonth(TimeHandler.nowDate().lengthOfMonth())
        )
}