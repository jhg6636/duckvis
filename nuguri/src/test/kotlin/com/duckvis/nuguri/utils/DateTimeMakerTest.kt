package com.duckvis.nuguri.utils

import com.duckvis.core.utils.DateTimeMaker
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.format.DateTimeParseException

class DateTimeMakerTest {
  @Test
  fun `숫자를 날짜로 변환한다`() {
//        println(TimeHandler.stringToDate("00700"))
    println(DateTimeMaker.stringToDate("1229"))
    println(DateTimeMaker.stringToDate("0803"))
    println(DateTimeMaker.stringToDate("210401"))
    println(DateTimeMaker.stringToDate("210529"))
    println(DateTimeMaker.stringToDate("0230"))
  }

  @Test
  fun `한글을 날짜로 변환한다`() {
    assertThrows<DateTimeParseException> {
      println(DateTimeMaker.stringToDate("ㅎㅇㅎㅇ"))
    }
  }
}