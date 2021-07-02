package com.duckvis.core.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

val String.toDayStartTime: LocalDateTime
  get() = LocalDateTime.of(DateTimeMaker.stringToDate(this), LocalTime.NOON).dayStartTime

val String.toDayEndTime: LocalDateTime
  get() = LocalDateTime.of(DateTimeMaker.stringToDate(this), LocalTime.NOON).dayEndTime

val String.dayToDates: List<LocalDate>
  get() {
    val days = this.split(",")
    return days.map { day -> DateTimeMaker.nowDateTime().monthEndTime.withDayOfMonth(day.toInt()).toLocalDate() }
  }

fun String.splitFirst(): String {
  return this.split(" ").first()
}

fun String.splitByMaximumLetterCount(max: Int): List<String> {
  return if (this.length <= max) {
    listOf(this)
  } else {
    val splitString = this.split("\n")
    val mutableList = mutableListOf<String>()
    val result = mutableListOf<String>()
    splitString.forEach { string ->
      if (mutableList.sumBy { it.length } + string.length > max) {
        result.add(mutableList.joinToString("\n"))
        mutableList.clear()
      }
      if (string.length > max) {
        result.add(mutableList.joinToString("\n"))
        mutableList.clear()
        for (cnt in 0 until string.length / max) {
          result.add(string.substring(max * cnt, max * (cnt + 1)))
        }
        result.add(string.substring(max * (string.length / max)))
      } else {
        mutableList.add(string)
      }
    }
    result.add(mutableList.joinToString("\n"))
    result.filter { it.isNotEmpty() }
  }
}