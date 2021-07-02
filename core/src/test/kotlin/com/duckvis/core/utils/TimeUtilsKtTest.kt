package com.duckvis.core.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TimeUtilsKtTest {

  @Test
  fun `음수 시간을 잘 스트링으로 변환한다`() {
    // given
    val minusSeconds1 = -764
    val minusSeconds2 = -33
    val minusSeconds3 = -10821

    // when
    val timeString1 = minusSeconds1.secondsToString
    val timeString2 = minusSeconds2.secondsToString
    val timeString3 = minusSeconds3.secondsToString

    // then
    assertThat(timeString1).isEqualTo("-12분 44초")
    assertThat(timeString2).isEqualTo("-33초")
    assertThat(timeString3).isEqualTo("-3시간 0분 21초")
  }

}