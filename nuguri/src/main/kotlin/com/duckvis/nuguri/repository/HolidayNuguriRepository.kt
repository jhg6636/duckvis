package com.duckvis.nuguri.repository

import com.duckvis.core.domain.nuguri.Holiday
import com.duckvis.core.domain.nuguri.QHoliday.holiday
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class HolidayNuguriRepository(
  @Autowired private val queryFactory: JPAQueryFactory,
) {
  fun getHolidays(startDay: LocalDate, endDay: LocalDate): List<Holiday> {
    return queryFactory
      .select(holiday)
      .from(holiday)
      .where(
        holiday.date.between(startDay, endDay)
      )
      .fetch()
  }

  fun leftDays(startDay: LocalDate, endDay: LocalDate): Int {
    return startDay.lengthOfMonth() - startDay.dayOfMonth - getHolidays(startDay, endDay).size
  }
}