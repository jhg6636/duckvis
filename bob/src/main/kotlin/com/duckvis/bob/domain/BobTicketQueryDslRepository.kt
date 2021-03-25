package com.duckvis.bob.domain

import com.duckvis.bob.domain.QBobTicket.bobTicket
import com.duckvis.bob.types.BobStyleType
import com.duckvis.bob.types.BobTimeType
import com.duckvis.core.utils.TimeHandler
import com.querydsl.core.types.dsl.BooleanExpression
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
    fun isFirst(mealType: BobTimeType, styleType: BobStyleType): Boolean {
        synchronized(this) {
            return queryFactory
                .select(bobTicket)
                .from(bobTicket)
                .where(
                    bobTicket.date.eq(TimeHandler.nowDate()),
                    bobTicket.bobTimeType.eq(mealType),
                    bobTicket.bobStyle.eq(styleType)
                )
                .fetchCount() == 1L
        }
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


private val isToday: BooleanExpression
    get() {
        return bobTicket.date.eq(TimeHandler.nowDate())
    }
