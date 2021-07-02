package com.duckvis.core.domain.bob

import com.duckvis.core.domain.BaseDuckvisEntity
import com.duckvis.core.types.bob.BobTimeType
import com.duckvis.core.types.bob.PayType
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
class UserPay(
  val userId: Long,
  private val dateTime: LocalDateTime,

  @Enumerated(value = EnumType.STRING)
  val bobTimeType: BobTimeType,

  @Enumerated(value = EnumType.STRING)
  var payBy: PayType,

  var money: Int,
  var isGotSupport: Boolean,
) : BaseDuckvisEntity() {

  val payString: String
    get() = "$dateTime ${money}원 ${payBy.korean}" + if (isGotSupport) {
      "지원"
    } else {
      "미지원"
    }

  fun eatSelf(money: Int) {
    this.payBy = PayType.MEMBER_SELF
    this.money = money
    this.isGotSupport = false
  }

  fun paidMoneyWithSupport(money: Int) {
    this.payBy = PayType.SELECTSTAR
    this.money = money
    this.isGotSupport = true
  }

  fun paidMoneyWithoutSupport(money: Int) {
    this.payBy = PayType.SELECTSTAR
    this.money = money
    this.isGotSupport = false
  }
}