package com.duckvis.nuguri.utils

import com.duckvis.core.utils.toDuration
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.format.DateTimeParseException

class TimeUtilsTest {
  @Test
  fun `한글을 duration으로 변환한다`() {
    org.junit.jupiter.api.assertThrows<DateTimeParseException> {
      println("ㄱ ㄱㅂ".toDuration)
    }
  }

  @Test
  fun `숫자를 duration으로 변환한다`() {
    println("02".toDuration)
    println("2:9".toDuration)
    println("11:18".toDuration)
  }

  @Test
  fun x() {
    println(LocalDate.of(2021, 3, 31).minusMonths(6))
  }
}