package com.duckvis.core.dtos.nuguri

data class WorkTypeDto(
  val isNight: Boolean = false,
  val isExtended: Boolean = false,
  val isHoliday: Boolean = false
) {

  companion object {
    fun of(options: List<String>): WorkTypeDto {
      val isNight = options.contains("%야간")
      val isExtended = options.contains("%연장")
      val isHoliday = options.contains("%휴일")

      return WorkTypeDto(isNight, isExtended, isHoliday)
    }
  }

}