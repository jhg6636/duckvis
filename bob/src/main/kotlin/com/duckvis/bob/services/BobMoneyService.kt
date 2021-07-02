package com.duckvis.bob.services

import com.duckvis.core.domain.bob.*
import com.duckvis.core.exceptions.NotMatchingPayTypeException
import com.duckvis.core.types.bob.BobTimeType
import com.duckvis.core.types.bob.PayType
import com.duckvis.core.utils.DateTimeMaker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Integer.max
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class BobMoneyService(
  @Autowired private val overMoneyRepository: OverMoneyRepository,
  @Autowired private val overMoneyQueryDslRepository: OverMoneyQueryDslRepository,
  @Autowired private val userPayRepository: UserPayRepository,
  @Autowired private val userPayQueryDslRepository: UserPayQueryDslRepository,
  @Autowired private val bobHistoryQueryDslRepository: BobHistoryQueryDslRepository,
) {
  companion object {
    const val ONE_MEAL = 10000
  }

  @Transactional
  fun responsePayWithSupport(
    userId: Long,
    money: Int,
    now: LocalDateTime = DateTimeMaker.nowDateTime(),
    bobTimeType: BobTimeType = BobTimeType.payRecordTime(now)
  ): String {
    deleteAlreadyPay(userId, now.toLocalDate(), bobTimeType)
    val hasPaid = havePaidToday(userId, now.toLocalDate())
    deleteAlreadyOverMoney(userId, now.toLocalDate(), bobTimeType)
    checkSamePayType(userId, now.toLocalDate(), bobTimeType, PayType.SELECTSTAR)
    userPayRepository.save(UserPay(userId, now, bobTimeType, PayType.SELECTSTAR, money, true))
    if (money > ONE_MEAL) {
      overMoneyRepository.save(OverMoney(userId, money - ONE_MEAL, now, bobTimeType))
    }
    val adding = max(0, money - ONE_MEAL)

    val hasPaidCheckMessage = if (hasPaid) {
      "오늘 이미 지원받으셨어요옹~ 추가지원이 맞으신지 한 번 더 확인 부탁드려요옹~\n"
    } else {
      ""
    }

    return ":credit_card:입력해 주신 금액은 ${money}원 (지원) 이에요옹~ 초과금 ${adding}원이 추가되었어요옹~\n" +
      ":moneybag:현재 누적된 초과금은 ${responseTotalOverMoney(userId)}원이에요옹~\n" +
      "$hasPaidCheckMessage"
  }

  @Transactional
  fun responseModifyPayWithSupport(
    userId: Long,
    bobTimeType: BobTimeType,
    money: Int,
    date: LocalDate = DateTimeMaker.nowDate()
  ): String {
    deleteAlreadyPay(userId, date, bobTimeType)
    val hasPaid = havePaidToday(userId, date)
    deleteAlreadyOverMoney(userId, date, bobTimeType)
    checkSamePayType(userId, date, bobTimeType, PayType.SELECTSTAR)
    if (money > ONE_MEAL) {
      overMoneyRepository.save(
        OverMoney(
          userId,
          money - ONE_MEAL,
          LocalDateTime.of(date, LocalTime.of(bobTimeType.endHour, 30)),
          bobTimeType
        )
      )
    }
    userPayRepository.save(
      UserPay(
        userId,
        LocalDateTime.of(date, LocalTime.of(bobTimeType.endHour, 30)),
        bobTimeType,
        PayType.SELECTSTAR,
        money,
        true
      )
    )

    val hasPaidCheckMessage = if (hasPaid) {
      "이날 이미 지원받으셨어요옹~ 추가지원이 맞으신지 한 번 더 확인 부탁드려요옹~\n"
    } else {
      ""
    }

    return ":credit_card:수정된 금액은 ${money}원 (지원) 이에요옹~ 초과금이 ${max(money - ONE_MEAL, 0)}원으로 수정되었어요옹~\n" +
      ":moneybag:현재 누적된 초과금은 ${responseTotalOverMoney(userId)}원이에요옹~\n" +
      "$hasPaidCheckMessage"
  }

  @Transactional
  fun responsePayWithoutSupport(
    userId: Long,
    money: Int,
    now: LocalDateTime = DateTimeMaker.nowDateTime(),
    bobTimeType: BobTimeType = BobTimeType.payRecordTime(now)
  ): String {
    deleteAlreadyPay(userId, now.toLocalDate(), bobTimeType)
    deleteAlreadyOverMoney(userId, now.toLocalDate(), bobTimeType)
    checkSamePayType(userId, now.toLocalDate(), bobTimeType, PayType.SELECTSTAR)
    userPayRepository.save(UserPay(userId, now, bobTimeType, PayType.SELECTSTAR, money, false))
    overMoneyRepository.save(OverMoney(userId, money, now, bobTimeType))
    return ":credit_card:입력해 주신 금액은 ${money}원 (미지원) 이에요옹~ 초과금 ${money}원이 추가되었어요옹~\n" +
      ":moneybag:현재 누적된 초과금은 ${responseTotalOverMoney(userId)}원이에요옹~\n"
  }

  @Transactional
  fun responseModifyPayWithoutSupport(
    userId: Long,
    bobTimeType: BobTimeType,
    money: Int,
    date: LocalDate = DateTimeMaker.nowDate(),
  ): String {
    deleteAlreadyPay(userId, date, bobTimeType)
    deleteAlreadyOverMoney(userId, date, bobTimeType)
    checkSamePayType(userId, date, bobTimeType, PayType.SELECTSTAR)

    userPayRepository.save(
      UserPay(
        userId,
        LocalDateTime.of(date, LocalTime.of(bobTimeType.endHour, 30)),
        bobTimeType,
        PayType.SELECTSTAR,
        money,
        false
      )
    )
    overMoneyRepository.save(
      OverMoney(
        userId,
        money,
        LocalDateTime.of(date, LocalTime.of(bobTimeType.endHour, 30)),
        bobTimeType
      )
    )

    return ":credit_card:수정된 금액은 ${money}원 (미지원) 이에요옹~ 초과금이 ${money}원으로 수정되었어요옹~\n" +
      ":moneybag:현재 누적된 초과금은 ${responseTotalOverMoney(userId)}원이에요옹~\n"
  }

  @Transactional
  fun responseSelfPayWithSupport(
    userId: Long,
    deliveryCost: Int = 0,
    now: LocalDateTime = DateTimeMaker.nowDateTime(),
    bobTimeType: BobTimeType = BobTimeType.payRecordTime(now)
  ): String {
    deleteAlreadyPay(userId, now.toLocalDate(), bobTimeType)
    val hasPaid = havePaidToday(userId, now.toLocalDate())
    deleteAlreadyOverMoney(userId, now.toLocalDate(), bobTimeType)
    checkSamePayType(userId, now.toLocalDate(), bobTimeType, PayType.MEMBER_SELF)

    userPayRepository.save(UserPay(userId, now, bobTimeType, PayType.MEMBER_SELF, -deliveryCost - ONE_MEAL, true))
    paidOneMealSelf(userId, deliveryCost, now, bobTimeType)

    val hasPaidCheckMessage = if (hasPaid) {
      "오늘 이미 지원받으셨어요옹~ 추가지원이 맞으신지 한 번 더 확인 부탁드려요옹~\n"
    } else {
      ""
    }

    return ":credit_card:개인결제하셨군요옹~ 초과금에서 ${ONE_MEAL + deliveryCost}원이 차감되었어요옹~\n" +
      ":moneybag:현재 누적된 초과금은 ${responseTotalOverMoney(userId)}원이에요옹~\n" +
      "$hasPaidCheckMessage"
  }

  @Transactional
  fun responseSelfPayWithoutSupport(
    userId: Long,
    deliveryCost: Int = 0,
    now: LocalDateTime = DateTimeMaker.nowDateTime(),
    bobTimeType: BobTimeType = BobTimeType.payRecordTime(now)
  ): String {
    deleteAlreadyPay(userId, now.toLocalDate(), bobTimeType)
    deleteAlreadyOverMoney(userId, now.toLocalDate(), bobTimeType)
    checkSamePayType(userId, now.toLocalDate(), bobTimeType, PayType.MEMBER_SELF)

    userPayRepository.save(UserPay(userId, now, bobTimeType, PayType.MEMBER_SELF, -deliveryCost, false))
    overMoneyRepository.save(OverMoney(userId, -deliveryCost, now, bobTimeType))
    return ":credit_card:개인결제하셨군요옹~ 초과금에서 ${deliveryCost}원이 차감되었어요옹~\n" +
      ":moneybag:현재 누적된 초과금은 ${responseTotalOverMoney(userId)}원이에요옹~\n"
  }

  @Transactional
  fun responseModifySelfPayWithSupport(
    userId: Long,
    bobTimeType: BobTimeType,
    deliveryCost: Int = 0,
    date: LocalDate = DateTimeMaker.nowDate(),
  ): String {
    deleteAlreadyPay(userId, date, bobTimeType)
    val hasPaid = havePaidToday(userId, date)
    deleteAlreadyOverMoney(userId, date, bobTimeType)
    checkSamePayType(userId, date, bobTimeType, PayType.MEMBER_SELF)

    userPayRepository.save(
      UserPay(
        userId,
        LocalDateTime.of(date, LocalTime.of(bobTimeType.endHour, 30)),
        bobTimeType,
        PayType.MEMBER_SELF,
        -10000,
        true
      )
    )
    paidOneMealSelf(userId, deliveryCost, LocalDateTime.of(date, LocalTime.of(bobTimeType.endHour, 30)), bobTimeType)

    val hasPaidCheckMessage = if (hasPaid) {
      "이날 이미 지원받으셨어요옹~ 추가지원이 맞으신지 한 번 더 확인 부탁드려요옹~\n"
    } else {
      ""
    }

    return ":credit_card:개인결제하셨군요옹~ 초과금에서 ${ONE_MEAL + deliveryCost}원 차감되었어요옹~\n" +
      ":moneybag:현재 누적된 초과금은 ${responseTotalOverMoney(userId)}원이에요옹~\n" +
      "$hasPaidCheckMessage"
  }

  @Transactional
  fun responseModifySelfPayWithoutSupport(
    userId: Long,
    bobTimeType: BobTimeType,
    deliveryCost: Int = 0,
    date: LocalDate = DateTimeMaker.nowDate(),
  ): String {
    deleteAlreadyPay(userId, date, bobTimeType)
    deleteAlreadyOverMoney(userId, date, bobTimeType)
    checkSamePayType(userId, date, bobTimeType, PayType.MEMBER_SELF)

    userPayRepository.save(
      UserPay(
        userId,
        LocalDateTime.of(date, LocalTime.of(bobTimeType.endHour, 30)),
        bobTimeType,
        PayType.MEMBER_SELF,
        -deliveryCost,
        false
      )
    )
    overMoneyRepository.save(
      OverMoney(
        userId,
        -deliveryCost,
        LocalDateTime.of(date, LocalTime.of(bobTimeType.endHour, 30)),
        bobTimeType
      )
    )

    return ":credit_card:개인결제하셨군요옹~ 초과금에서 ${deliveryCost}원 차감되었어요옹~\n" +
      ":moneybag:현재 누적된 초과금은 ${responseTotalOverMoney(userId)}원이에요옹~\n"
  }

  @Transactional
  fun responseTotalOverMoney(userId: Long): Int {
    return overMoneyRepository.findAllByUserId(userId).sumBy { overMoney -> overMoney.money }
  }

  @Transactional
  fun paidOneMealSelf(
    userId: Long,
    deliveryCost: Int = 0,
    now: LocalDateTime = DateTimeMaker.nowDateTime(),
    bobTimeType: BobTimeType = BobTimeType.payRecordTime(now)
  ): OverMoney {
    val total = responseTotalOverMoney(userId)
    val mealMoney = max(-total, -ONE_MEAL)
    return overMoneyRepository.save(OverMoney(userId, mealMoney - deliveryCost, now, bobTimeType))
  }

  @Transactional
  fun getPayType(userId: Long): PayType {
    return PayType.by(responseTotalOverMoney(userId) >= ONE_MEAL)
  }

  fun checkSamePayType(userId: Long, date: LocalDate, bobTimeType: BobTimeType, payType: PayType) {
    val thisMealPayType = bobHistoryQueryDslRepository.getThisMealPayType(userId, date, bobTimeType)
    if (thisMealPayType != null && thisMealPayType != payType) {
      throw NotMatchingPayTypeException()
    }
  }

  private fun deleteAlreadyPay(userId: Long, date: LocalDate, bobTimeType: BobTimeType) {
    val pay = userPayQueryDslRepository.getPayToModify(userId, date, bobTimeType)
    if (pay != null) {
      userPayRepository.delete(pay)
    }
  }

  private fun deleteAlreadyOverMoney(userId: Long, date: LocalDate, bobTimeType: BobTimeType) {
    val originalOverMoney = overMoneyQueryDslRepository.thisMealOverMoney(userId, date, bobTimeType)
    if (originalOverMoney != null) {
      overMoneyRepository.delete(originalOverMoney)
    }
  }

  @Transactional
  fun havePaidToday(userId: Long, date: LocalDate): Boolean {
    return userPayQueryDslRepository.hasTodayPay(userId, date)
  }

  @Transactional
  fun responseShowAllPay(userId: Long, size: Int): List<UserPay> {
    return userPayQueryDslRepository.getMyRecentPays(userId, size)
  }

}