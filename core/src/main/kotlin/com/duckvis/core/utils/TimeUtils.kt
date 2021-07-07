package com.duckvis.core.utils

import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import java.time.*
import java.time.format.DateTimeFormatter
import kotlin.math.round

val LocalDate.isNationalHoliday: Boolean
  get() = listOf(
    LocalDate.of(year, 1, 1),
    LocalDate.of(year, 3, 1),
    LocalDate.of(year, 5, 5),
    LocalDate.of(year, 6, 6),
    LocalDate.of(year, 8, 15),
    LocalDate.of(year, 10, 3),
    LocalDate.of(year, 10, 9),
    LocalDate.of(year, 12, 25)
  ).contains(this)

val LocalDate.isWeekend: Boolean
  get() = this.dayOfWeek == DayOfWeek.SATURDAY || this.dayOfWeek == DayOfWeek.SUNDAY

val LocalDate.isWeekday: Boolean
  get() = !this.isWeekend

val LocalDate.toKorean
  get() = "${this.monthValue}월 ${this.dayOfMonth}일"

val LocalDateTime?.dateTimeString: String
  get() {
    if (this == null) {
      return "해당없음"
    }
    return this.plusHours(9).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
  }

val Int?.secondsToString: String
  get() {
    if (this == null) {
      return "0시간 0분 0초"
    }
    return if (this < 0) {
      val inverseSecond = -this
      when {
        inverseSecond / 3600 > 0 -> "-${inverseSecond / 3600}시간 ${(inverseSecond % 3600) / 60}분 ${inverseSecond % 60}초"
        inverseSecond / 60 > 0 -> "-${inverseSecond / 60}분 ${inverseSecond % 60}초"
        else -> "-${inverseSecond}초"
      }
    } else {
      when {
        this / 3600 > 0 -> "${this / 3600}시간 ${this % 3600 / 60}분 ${this % 60}초"
        this / 60 > 0 -> "${this / 60}분 ${this % 60}초"
        else -> "${this}초"
      }
    }
  }

val Int?.secondsToShortString: String
  get() {
    if (this == null) {
      return "0:0:0"
    }
    return if (this < 0) {
      val inverseSecond = -this
      when {
        inverseSecond / 3600 > 0 -> "-${inverseSecond / 3600}:${(inverseSecond % 3600) / 60}:${inverseSecond % 60}"
        inverseSecond / 60 > 0 -> "-0:${this / 60}:${this % 60}"
        else -> "-0:0:${this}"
      }
    } else {
      when {
        this / 3600 > 0 -> "${this / 3600}:${this % 3600 / 60}:${this % 60}"
        this / 60 > 0 -> "0:${this / 60}:${this % 60}"
        else -> "0:0:${this}"
      }
    }
  }

val Int.secondsToHours: Double
  get() = round(this / 3600.0 * 100) / 100

val String.toDurationSeconds: Int
  get() {
    val splitString = this.split(":")
    if (splitString.size > 2) {
      throw NuguriException(ExceptionType.TYPO)
    }
    val hours = splitString.first().toInt() * 60 * 60
    val minutes = (splitString.getOrNull(1)?.toInt() ?: 0) * 60
    return hours + minutes
  }
