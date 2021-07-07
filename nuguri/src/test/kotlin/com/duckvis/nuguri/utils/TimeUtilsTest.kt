package com.duckvis.nuguri.utils

import com.duckvis.core.utils.toDurationSeconds
import org.junit.jupiter.api.Test
import java.lang.NumberFormatException
import java.time.LocalDate
import java.time.format.DateTimeParseException

class TimeUtilsTest {
  @Test
  fun `한글을 duration으로 변환한다`() {
    org.junit.jupiter.api.assertThrows<NumberFormatException> {
      println("ㄱ ㄱㅂ".toDurationSeconds)
    }
  }

  @Test
  fun `숫자를 duration으로 변환한다`() {
    println("02".toDurationSeconds)
    println("2:9".toDurationSeconds)
    println("11:18".toDurationSeconds)
  }

  @Test
  fun x() {
    println(LocalDate.of(2021, 3, 31).minusMonths(6))
  }
}