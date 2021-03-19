package com.catshi.bob.domain

import com.catshi.bob.domain.QBobHistory.bobHistory
import com.catshi.bob.types.BobTimeType
import com.catshi.core.domain.User
import com.catshi.core.utils.TimeHandler
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
        eatDates.forEach {
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

    private val isThisMonth = bobHistory.bobTicket.date.between(TimeHandler.nowDate().withDayOfMonth(1),
        TimeHandler.nowDate().withDayOfMonth(TimeHandler.nowDate().lengthOfMonth()))
}
