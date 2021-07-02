package com.duckvis.core.domain.bob

import com.duckvis.core.domain.bob.QBobTicket.bobTicket
import com.duckvis.core.types.bob.BobStyleType
import com.duckvis.core.types.bob.BobTimeType
import com.duckvis.core.types.bob.PayType
import com.duckvis.core.types.shared.CityType
import com.duckvis.core.utils.DateTimeMaker
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

/**
 * A <- core
 * B <- core
 *
 * Entity (core) EntityRepository : JpaRepository (core)
 *
 * A module : EntityARepository
 * B module : EntityBRepository
 *
 * A, B 둘다 필요한 쿼리가 경우
 * 방법 1) core Repository에 querydsl 을 넣는다 (새로운 class 탄생)
 *   애매해다. core에 querydsl 의존성이 들어가도 되는가? POJO
 *
 * 방법 2) A와 B에 코드를 중복해서 넣는다
 *   안좋다. 유지보수할때 둘 다 고쳐야한다. 좋을 수도 있다. (중복이 꼭 나쁜 것은 아니다. 케바케)
 *   하지만 일반적으로 안좋죠 ^^
 *
 * 방법 3) core에서 JPQL을 쓴다 (기존 EntityRepository 활용)
 *   Entity 변경에 취약하다 -> querydsl은? IntelliJ가 자동으로 바꿔주는 부분
 *   왜 querydsl을 사용하는가 >> TODO 고민해보시면 좋을 것 같아요 ^^
 *
 * 방법 4) 중간 모듈을 만들어 공통된 로직을 넣는다
 *   괜찮을 것 같다. -> 둘 모두 사용할 수 있기 때문.
 *   단점도 있어요 >> TODO 고민해보시면 좋을 것 같아요 ^^
 */
@Component
class BobTicketQueryDslRepository(
  private val queryFactory: JPAQueryFactory,
) {

  fun isExistingTicket(userId: Long, thisMeal: BobTimeType, styleType: BobStyleType, cityType: CityType): Boolean {
    return queryFactory
      .select(bobTicket)
      .from(bobTicket)
      .where(
        bobTicket.userId.eq(userId),
        bobTicket.bobTimeType.eq(thisMeal),
        bobTicket.bobStyle.eq(styleType),
        bobTicket.city.eq(cityType),
        isToday
      )
      .fetchOne() != null
  }

  @Transactional
  fun getThisMealTicket(userId: Long, thisMeal: BobTimeType): BobTicket? {
    return queryFactory
      .select(bobTicket)
      .from(bobTicket)
      .where(
        bobTicket.userId.eq(userId),
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
  fun findThisMealTicketsWithPayType(mealType: BobTimeType, payType: PayType): List<BobTicket> {
    return queryFactory
      .select(bobTicket)
      .from(bobTicket)
      .where(
        isToday,
        bobTicket.bobTimeType.eq(mealType),
        bobTicket.payBy.eq(payType)
      )
      .fetch()
  }

  @Transactional(isolation = Isolation.SERIALIZABLE)
  fun isFirst(mealType: BobTimeType, styleType: BobStyleType): Boolean {
    return styleType != BobStyleType.LATE_EAT && (queryFactory
      .select(bobTicket)
      .from(bobTicket)
      .where(
        isToday,
        bobTicket.bobTimeType.eq(mealType),
        bobTicket.bobStyle.eq(styleType)
      )
      .fetchCount() == 1L)
  }
}


private val isToday: BooleanExpression
  get() {
    return bobTicket.date.eq(DateTimeMaker.nowDate())
  }
