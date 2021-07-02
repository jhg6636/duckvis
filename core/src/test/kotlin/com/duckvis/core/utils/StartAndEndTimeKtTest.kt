package com.duckvis.core.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class StartAndEndTimeKtTest {

  @Test
  fun `이번 주 시작 시간?`() {
    val time = LocalDateTime.now().minusHours(13)
    println(time.weekStartTime)
    println(time.weekEndTime)
  }

}