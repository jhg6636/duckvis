package com.duckvis.core.domain.nuguri

import com.duckvis.core.domain.BaseDuckvisEntity
import com.duckvis.core.types.nuguri.HolidayType
import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
class Holiday(
  private val date: LocalDate,

  @Enumerated(EnumType.STRING)
  private var type: HolidayType,
) : BaseDuckvisEntity() {

  val dayString: String
    get() = this.date.dayOfMonth.toString()

  val dayOfMonth: Int
    get() = this.date.dayOfMonth

  val isWeekend: Boolean
    get() = type == HolidayType.WEEKEND

  fun setWeekend() {
    this.type = HolidayType.WEEKEND
  }

}