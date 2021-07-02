package com.duckvis.core.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

object DateTimeMaker {

  fun nowDateTime(): LocalDateTime {
    return LocalDateTime.now(ZoneOffset.UTC)
  }

  fun nowDate(): LocalDate {
    return nowDateTime().toLocalDate()
  }

  fun stringToDate(from: String): LocalDate {
    val formatter = if (from.length == 4) {
      DateTimeFormatterBuilder()
        .appendPattern("MMdd")
        .parseDefaulting(ChronoField.YEAR, nowDate().year.toLong())
        .toFormatter()
    } else {
      DateTimeFormatter.ofPattern("yyMMdd")
    }

    return LocalDate.parse(from, formatter)
  }

}