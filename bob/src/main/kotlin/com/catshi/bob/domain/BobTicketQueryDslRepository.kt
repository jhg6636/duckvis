package com.catshi.bob.domain

import com.catshi.bob.domain.QBobTicket.*
import com.catshi.bob.types.BobStyleType
import com.catshi.bob.types.BobTimeType
import com.catshi.core.types.CityType
import com.catshi.core.utils.TimeHandler
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class BobTicketQueryDslRepository(
    private val queryFactory: JPAQueryFactory,
) {

    @Transactional
    fun getThisMealTicket(userId: Long, thisMeal: BobTimeType): BobTicket? {
        return queryFactory
            .select(bobTicket)
            .from(bobTicket)
            .where(
                bobTicket.user.id.eq(userId),
                bobTicket.bobTimeType.eq(thisMeal),
                isToday
            )
            .fetchOne()
    }

    @Transactional
    fun findThisMealAllTickets(mealType: BobTimeType): List<BobTicket> {
        return queryFactory
            .select(bobTicket)
            .from(bobTicket)
            .where(
                isToday,
                bobTicket.bobTimeType.eq(mealType)
            )
            .fetch()
    }

    @Transactional
    fun isFirst(mealType: BobTimeType): Boolean {
        return queryFactory
            .select(bobTicket)
            .from(bobTicket)
            .where(
                isToday,
                bobTicket.bobTimeType.eq(mealType)
            )
            .fetchCount() == 1L
    }

    @Transactional
    fun hasMoreThanOneTicket(userId: Long, mealType: BobTimeType): Boolean {
        return queryFactory
            .select(bobTicket)
            .from(bobTicket)
            .where(
                isToday,
                bobTicket.bobTimeType.eq(mealType),
                bobTicket.user.id.eq(userId)
            )
            .fetchCount() > 1L
    }
}

private val isToday = bobTicket.date.eq(TimeHandler.nowDate())
