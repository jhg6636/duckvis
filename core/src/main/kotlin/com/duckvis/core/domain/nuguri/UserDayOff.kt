package com.duckvis.core.domain.nuguri

import com.duckvis.core.domain.BaseDuckvisEntity
import com.duckvis.core.types.nuguri.DayOffType
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
class UserDayOff(
  private val userId: Long,
  private val days: Int,

  @Enumerated(EnumType.STRING)
  private val type: DayOffType,
) : BaseDuckvisEntity() {
}