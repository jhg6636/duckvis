package com.duckvis.bob.services

import com.duckvis.core.domain.bob.OverMoney
import com.duckvis.core.domain.bob.OverMoneyRepository
import com.duckvis.core.domain.bob.UserPayRepository
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.types.bob.BobTimeType
import com.duckvis.core.types.bob.PayType
import com.duckvis.core.utils.DateTimeMaker
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
class BobMoneyServiceTest constructor(
  @Autowired private val bobMoneyService: BobMoneyService,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val overMoneyRepository: OverMoneyRepository,
  @Autowired private val userPayRepository: UserPayRepository,
) {
  @BeforeEach
  @Transactional
  fun prepare() {
    userRepository.save(User("user1", "user1"))
    userRepository.save(User("user2", "user2"))
  }

  @AfterEach
  fun clean() {
    overMoneyRepository.deleteAllInBatch()
    userPayRepository.deleteAllInBatch()
    userRepository.deleteAllInBatch()
  }

  @Test
  fun `!지원 12000 을 입력한다`() {
    // given
    val user = userRepository.findByName("user1")

    // when
    val string = bobMoneyService.responsePayWithSupport(
      user!!.id, 12000, DateTimeMaker.nowDateTime().withHour(3),
    )
    val userPay = userPayRepository.findAll()[0]
    val overMoney = overMoneyRepository.findAll()[0]

    // then
    assertThat(string).isEqualTo(
      ":credit_card:입력해 주신 금액은 12000원 (지원) 이에요옹~ 초과금 2000원이 추가되었어요옹~\n" +
        ":moneybag:현재 누적된 초과금은 2000원이에요옹~\n"
    )
    assertThat(overMoneyRepository.count()).isEqualTo(1)
    assertThat(overMoney!!.money).isEqualTo(2000)
    assertThat(userPayRepository.count()).isEqualTo(1)
    assertThat(userPay!!.money).isEqualTo(12000)
    assertThat(userPay!!.isGotSupport).isTrue
    assertThat(userPay!!.payBy).isEqualTo(PayType.SELECTSTAR)
  }

  @Test
  fun `!지원 8000 을 입력한다`() {
    // given
    val user = userRepository.findByName("user1")

    // when
    val string = bobMoneyService.responsePayWithSupport(user!!.id, 8000, DateTimeMaker.nowDateTime().withHour(3))
    val userPay = userPayRepository.findAll()[0]

    // then
    assertThat(string).isEqualTo(
      ":credit_card:입력해 주신 금액은 8000원 (지원) 이에요옹~ 초과금 0원이 추가되었어요옹~\n" +
        ":moneybag:현재 누적된 초과금은 0원이에요옹~\n"
    )
    assertThat(overMoneyRepository.count()).isEqualTo(0)
    assertThat(userPayRepository.count()).isEqualTo(1)
    assertThat(userPay!!.money).isEqualTo(8000)
    assertThat(userPay!!.isGotSupport).isTrue
    assertThat(userPay!!.payBy).isEqualTo(PayType.SELECTSTAR)
  }

  @Test
  fun `!미지원 9000 을 입력한다`() {
    // given
    val user = userRepository.findByName("user1")

    // when
    val string = bobMoneyService.responsePayWithoutSupport(user!!.id, 9000, DateTimeMaker.nowDateTime().withHour(3))
    val userPay = userPayRepository.findAll()[0]
    val overMoney = overMoneyRepository.findAll()[0]

    // then
    assertThat(string).isEqualTo(
      ":credit_card:입력해 주신 금액은 9000원 (미지원) 이에요옹~ 초과금 9000원이 추가되었어요옹~\n" +
        ":moneybag:현재 누적된 초과금은 9000원이에요옹~\n"
    )
    assertThat(overMoneyRepository.count()).isEqualTo(1)
    assertThat(overMoney!!.money).isEqualTo(9000)
    assertThat(userPayRepository.count()).isEqualTo(1)
    assertThat(userPay!!.money).isEqualTo(9000)
    assertThat(userPay!!.isGotSupport).isFalse
    assertThat(userPay!!.payBy).isEqualTo(PayType.SELECTSTAR)
  }

  @Test
  fun `13000원의 초과금이 있는 사람의 PayType`() {
    // given
    val user = userRepository.findByName("user1")
    overMoneyRepository.save(OverMoney(user!!.id, 13000, DateTimeMaker.nowDateTime(), BobTimeType.LUNCH))

    // when
    val payType = bobMoneyService.getPayType(user!!.id)

    // then
    assertThat(payType).isEqualTo(PayType.MEMBER_SELF)
  }

  @Test
  fun `개인결제 10000을 입력한다 (지원)`() {
    // given
    val user = userRepository.findByName("user1")
    overMoneyRepository.save(OverMoney(user!!.id, 13000, DateTimeMaker.nowDateTime().minusDays(2), BobTimeType.LUNCH))

    // when
    val string = bobMoneyService.responseSelfPayWithSupport(user!!.id, 0, DateTimeMaker.nowDateTime().withHour(3))
    val userPay = userPayRepository.findAll()[0]
    val overMoney = overMoneyRepository.findAll()[1]

    // then
    assertThat(string).isEqualTo(
      ":credit_card:개인결제하셨군요옹~ 초과금에서 10000원이 차감되었어요옹~\n" +
        ":moneybag:현재 누적된 초과금은 3000원이에요옹~\n"
    )
    assertThat(overMoneyRepository.count()).isEqualTo(2)
    assertThat(overMoney!!.money).isEqualTo(-10000)
    assertThat(userPayRepository.count()).isEqualTo(1)
    assertThat(userPay!!.money).isEqualTo(-10000)
    assertThat(userPay!!.isGotSupport).isTrue
    assertThat(userPay!!.payBy).isEqualTo(PayType.MEMBER_SELF)
  }

  @Test
  fun `!배달비 700 %미지원을 입력한다`() {
    // given
    val user = userRepository.findByName("user1")
    overMoneyRepository.save(OverMoney(user!!.id, 13000, DateTimeMaker.nowDateTime().minusDays(2), BobTimeType.LUNCH))

    // when
    val string = bobMoneyService.responseSelfPayWithoutSupport(user!!.id, 700, DateTimeMaker.nowDateTime().withHour(3))
    val userPay = userPayRepository.findAll()[0]
    val overMoney = overMoneyRepository.findAll()[1]

    // then
    assertThat(string).isEqualTo(
      ":credit_card:개인결제하셨군요옹~ 초과금에서 700원이 차감되었어요옹~\n" +
        ":moneybag:현재 누적된 초과금은 12300원이에요옹~\n"
    )
    assertThat(overMoneyRepository.count()).isEqualTo(2)
    assertThat(overMoney!!.money).isEqualTo(-700)
    assertThat(userPayRepository.count()).isEqualTo(1)
    assertThat(userPay!!.money).isEqualTo(-700)
    assertThat(userPay!!.isGotSupport).isFalse
    assertThat(userPay!!.payBy).isEqualTo(PayType.MEMBER_SELF)
  }

  @Test
  fun `지원 8000원이었던 것을 미지원 8000 으로 수정한다`() {
    // given
    val user = userRepository.findByName("user1")
    bobMoneyService.responsePayWithSupport(user!!.id, 8000, DateTimeMaker.nowDateTime().withHour(3))

    // when
    val string = bobMoneyService.responseModifyPayWithoutSupport(user!!.id, BobTimeType.LUNCH, 8000)
    val userPay = userPayRepository.findAll()[0]
    val overMoney = overMoneyRepository.findAll()[0]

    // then
    assertThat(string).isEqualTo(
      ":credit_card:수정된 금액은 8000원 (미지원) 이에요옹~ 초과금이 8000원으로 수정되었어요옹~\n" +
        ":moneybag:현재 누적된 초과금은 8000원이에요옹~\n"
    )
    assertThat(overMoneyRepository.count()).isEqualTo(1)
    assertThat(overMoney!!.money).isEqualTo(8000)
    assertThat(userPayRepository.count()).isEqualTo(1)
    assertThat(userPay!!.money).isEqualTo(8000)
    assertThat(userPay!!.isGotSupport).isFalse
    assertThat(userPay!!.payBy).isEqualTo(PayType.SELECTSTAR)
  }

  @Test
  fun `미지원 8000원이었던 것을 지원 10000으로 수정한다`() {
    // given
    val user = userRepository.findByName("user1")
    bobMoneyService.responsePayWithoutSupport(user!!.id, 8000, DateTimeMaker.nowDateTime().withHour(3))

    // when
    val string = bobMoneyService.responseModifyPayWithSupport(user!!.id, BobTimeType.LUNCH, 10000)
    val userPay = userPayRepository.findAll()[0]

    // then
    assertThat(string).isEqualTo(
      ":credit_card:수정된 금액은 10000원 (지원) 이에요옹~ 초과금이 0원으로 수정되었어요옹~\n" +
        ":moneybag:현재 누적된 초과금은 0원이에요옹~\n"
    )
    assertThat(overMoneyRepository.count()).isEqualTo(0)
    assertThat(userPayRepository.count()).isEqualTo(1)
    assertThat(userPay!!.money).isEqualTo(10000)
    assertThat(userPay!!.isGotSupport).isTrue
    assertThat(userPay!!.payBy).isEqualTo(PayType.SELECTSTAR)
  }

  @Test
  fun `개인결제였던 것을 지원 10000으로 수정한다`() {
    // given
    val user = userRepository.findByName("user1")
    bobMoneyService.responseSelfPayWithSupport(user!!.id, 8000, DateTimeMaker.nowDateTime().withHour(3))

    // when
    val string = bobMoneyService.responseModifyPayWithSupport(user!!.id, BobTimeType.LUNCH, 10000)
    val userPay = userPayRepository.findAll()[0]

    // then
    assertThat(string).isEqualTo(
      ":credit_card:수정된 금액은 10000원 (지원) 이에요옹~ 초과금이 0원으로 수정되었어요옹~\n" +
        ":moneybag:현재 누적된 초과금은 0원이에요옹~\n"
    )
    assertThat(overMoneyRepository.count()).isEqualTo(0)
    assertThat(userPayRepository.count()).isEqualTo(1)
    assertThat(userPay!!.money).isEqualTo(10000)
    assertThat(userPay!!.isGotSupport).isTrue
    assertThat(userPay!!.payBy).isEqualTo(PayType.SELECTSTAR)
  }

  @Test
  fun `이미 지원받았는지 확인한다`() {
    // given
    val user = userRepository.findByName("user1")
    bobMoneyService.responsePayWithSupport(user!!.id, 9800, DateTimeMaker.nowDateTime().minusDays(1).withHour(3))

    // when
    val already = bobMoneyService.havePaidToday(user!!.id, DateTimeMaker.nowDate())

    // then
    assertThat(already).isFalse
  }
}