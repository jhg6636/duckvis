package com.duckvis.nuguri.domain

import com.duckvis.core.utils.secondsToString
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AttendanceCardKtTest {
  @Test
  fun `1734초는 몇시간 몇 분 몇 초?`() {
    // when
    val timeString = 1734.secondsToString

    // then
    assertThat(timeString).isEqualTo("28분 54초")
  }

  @Test
  fun `11445초는 몇시간 몇 분 몇 초?`() {
    // when
    val timeString = 11445.secondsToString

    // then
    assertThat(timeString).isEqualTo("3시간 10분 45초")
  }
}