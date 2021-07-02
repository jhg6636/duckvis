package com.duckvis.bob.ui.command

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class ResponsePayWithSupportParserTest(
) {

  private val responsePayWithSupportParser = ResponsePayWithSupportParser()

  @Test
  fun `!금액 10900 %지원 210513 점심 은 null이다`() {
    assertThat(responsePayWithSupportParser.fromText("!금액 10900 %지원 210513 저녁")).isNull()
  }

}