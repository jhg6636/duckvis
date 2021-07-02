package com.duckvis.core.utils

import com.duckvis.core.utils.StartAndEndTime.CORE_TIME_END
import com.duckvis.core.utils.StartAndEndTime.CORE_TIME_START
import com.duckvis.core.utils.StartAndEndTime.DAY_START_AT
import com.duckvis.core.utils.StartAndEndTime.ERROR_SAFE_MARGIN
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

val LocalDateTime.dayStartTime: LocalDateTime
  get() {
    val errorSafeDateTime = this.plusHours(ERROR_SAFE_MARGIN)
    return LocalDateTime.of(errorSafeDateTime.toLocalDate().minusDays(1), LocalTime.of(DAY_START_AT, 0, 0))
  }

val LocalDateTime.dayEndTime: LocalDateTime
  get() = this.plusDays(1).dayStartTime

val LocalDateTime.weekStartTime: LocalDateTime
  get() {
    val errorSafeDateTime = this.plusHours(ERROR_SAFE_MARGIN)
    val date = errorSafeDateTime.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).toLocalDate()
    return LocalDateTime.of(date, LocalTime.of(DAY_START_AT, 0, 0))
  }

val LocalDateTime.weekEndTime: LocalDateTime
  get() = this.plusDays(7).weekStartTime

val LocalDateTime.monthStartTime: LocalDateTime
  get() {
    val errorSafeDateTime = this.plusHours(ERROR_SAFE_MARGIN)
    val date = errorSafeDateTime.with(TemporalAdjusters.firstDayOfMonth()).toLocalDate()
    return LocalDateTime.of(date.minusDays(1), LocalTime.of(DAY_START_AT, 0, 0))
  }

val LocalDateTime.monthEndTime: LocalDateTime
  get() = this.plusMonths(1).monthStartTime

val LocalDate.coreTimeStart: LocalDateTime
  get() = LocalDateTime.of(this, LocalTime.of(CORE_TIME_START, 0, 0))

val LocalDate.coreTimeEnd: LocalDateTime
  get() = LocalDateTime.of(this, LocalTime.of(CORE_TIME_END, 0, 0))

val LocalDateTime.coreTimeStart: LocalDateTime
  get() {
    val errorSafeDate = this.plusHours(ERROR_SAFE_MARGIN).toLocalDate()
    return LocalDateTime.of(errorSafeDate, LocalTime.of(CORE_TIME_START, 0, 0))
  }

val LocalDateTime.coreTimeEnd: LocalDateTime
  get() {
    val errorSafeDate = this.plusHours(ERROR_SAFE_MARGIN).toLocalDate()
    return LocalDateTime.of(errorSafeDate, LocalTime.of(CORE_TIME_END, 0, 0))
  }

object StartAndEndTime {
  const val DAY_START_AT = 21
  const val ERROR_SAFE_MARGIN = 3L
  const val CORE_TIME_START = 4
  const val CORE_TIME_END = 8
  const val CORE_TIME_DURATION = 4 * 60 * 60

  val coreTimeStart: LocalDateTime
    get() = DateTimeMaker.nowDateTime().coreTimeStart
  val coreTimeEnd: LocalDateTime
    get() = DateTimeMaker.nowDateTime().coreTimeEnd
}