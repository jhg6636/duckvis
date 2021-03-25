package com.duckvis.nuguri.dtos

class WorkTypeDto(
    val isNight: Boolean = false,
    val isExtended: Boolean = false,
    val isHoliday: Boolean = false,
) {
    override fun equals(other: Any?): Boolean {
        return other is WorkTypeDto && this.isNight == other.isNight && this.isExtended == other.isExtended && this.isHoliday == other.isHoliday
    }
}