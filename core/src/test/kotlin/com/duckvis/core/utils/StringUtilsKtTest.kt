package com.duckvis.core.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class StringUtilsKtTest {

  @Test
  fun `메시지 길이를 넘는 string을 잘 자른다`() {
    // given
    var sizeTwo = ""
    var sizeThree = ""
    for (i in 0..1501) {
      sizeTwo += "a"
    }

    for (i in 0..3333) {
      sizeThree += "3"
    }

    // when
    val splitString = sizeTwo.splitByMaximumLetterCount(1500)
    val splitString3 = sizeThree.splitByMaximumLetterCount(1500)

    // then
    assertThat(splitString).hasSize(2)
    assertThat(splitString3).hasSize(3)
  }

}