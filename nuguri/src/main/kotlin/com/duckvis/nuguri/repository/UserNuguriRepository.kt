package com.duckvis.nuguri.repository

import com.duckvis.core.domain.nuguri.QAttendanceCard.attendanceCard
import com.duckvis.core.domain.shared.QUser.user
import com.duckvis.core.domain.shared.User
import com.duckvis.core.types.nuguri.CardType
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component

@Component
class UserNuguriRepository(
  private val queryFactory: JPAQueryFactory,
) {

  fun getNotWorkingUsers(): List<User> {
    return queryFactory
      .select(user)
      .from(user)
      .leftJoin(attendanceCard)
      .on(
        attendanceCard.userId.eq(user.id),
        attendanceCard.logoutDateTime.isNull,
        attendanceCard.type.eq(CardType.WORK),
      )
      .where(
        attendanceCard.isNull
      )
      .fetch()
  }

  fun getMember(memberName: String?): User? {
    if (memberName == null) {
      return null
    }
    return queryFactory
      .select(user)
      .from(user)
      .where(
        user.name.eq(memberName)
      )
      .fetchOne()
  }

}
