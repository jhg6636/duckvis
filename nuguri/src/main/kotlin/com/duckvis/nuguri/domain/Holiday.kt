package com.duckvis.nuguri.domain

import com.duckvis.nuguri.types.HolidayType
import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Holiday(
    private val date: LocalDate,
    private val type: HolidayType,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = -1;

    fun isToday(date: LocalDate): Boolean {
        return this.date == date
    }
}