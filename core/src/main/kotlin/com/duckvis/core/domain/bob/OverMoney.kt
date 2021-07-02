package com.duckvis.core.domain.bob

import com.duckvis.core.domain.BaseDuckvisEntity
import com.duckvis.core.types.bob.BobTimeType
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

/**
 * 유저별로 초과금을 기록하기 위한 엔티티
 */
@Entity
class OverMoney(
  private val userId: Long,
  var money: Int,
  val dateTime: LocalDateTime,

  @Enumerated(value = EnumType.STRING)
  val bobTimeType: BobTimeType,
) : BaseDuckvisEntity() {
}