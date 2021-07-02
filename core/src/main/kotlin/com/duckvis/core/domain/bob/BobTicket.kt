package com.duckvis.core.domain.bob

import com.duckvis.core.domain.BaseDuckvisEntity
import com.duckvis.core.types.bob.BobStyleType
import com.duckvis.core.types.bob.BobTimeType
import com.duckvis.core.types.bob.IssuedOrderType
import com.duckvis.core.types.bob.PayType
import com.duckvis.core.types.shared.CityType
import org.springframework.boot.autoconfigure.domain.EntityScan
import java.time.LocalDate
import java.time.LocalTime
import javax.persistence.*

@Entity
@EntityScan(basePackages = ["com.duckvis.core"])
@Table(
  uniqueConstraints = [UniqueConstraint(
    name = "uni_bob_ticket_1",
    columnNames = ["date", "bob_time_type", "issued_order_number"]
  )]
)
class BobTicket(
  @Column(name = "user_id") // for test
  val userId: Long,

  val userName: String,
  val userTagString: String,

  val date: LocalDate,
  val time: LocalTime,

  @Enumerated(EnumType.STRING)
  @Column(name = "bob_time_type") // for test
  var bobTimeType: BobTimeType,

  @Enumerated(EnumType.STRING)
  var bobStyle: BobStyleType,

  @Enumerated(EnumType.STRING)
  val payBy: PayType,

  @Enumerated(EnumType.STRING)
  var city: CityType,

  @Column(name = "issued_order_number") // for test
  val issuedOrderNumber: Int,

  @Enumerated(EnumType.STRING)
  var issuedOrder: IssuedOrderType = IssuedOrderType.NOT_FIRST
) : BaseDuckvisEntity() {

  val isVegetarian: Boolean
    get() = this.bobStyle == BobStyleType.VEGETARIAN

  val isAnything: Boolean
    get() = this.bobStyle == BobStyleType.ANYTHING

  val isLateEat: Boolean
    get() = this.bobStyle == BobStyleType.LATE_EAT

  val isFirst: Boolean
    get() = this.issuedOrder == IssuedOrderType.FIRST

  val isSelfPay: Boolean
    get() = this.payBy == PayType.MEMBER_SELF

  fun isSamePay(payType: PayType): Boolean {
    return this.payBy == payType
  }

  fun setFirst() {
    this.issuedOrder = IssuedOrderType.FIRST
  }
}