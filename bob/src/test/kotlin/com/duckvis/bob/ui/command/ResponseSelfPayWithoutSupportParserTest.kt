package com.duckvis.bob.ui.command

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ResponseSelfPayWithoutSupportParserTest {
  @Test
  fun `!배달비 1000 %미지원`() {
    // given

    // when
    val parsed = ResponseSelfPayWithoutSupportParser().fromText("!배달비 1000 %미지원")

    // then
    assertThat(parsed).isNotNull
    assertThat(parsed).hasSameClassAs(ResponseSelfPayWithoutSupport(1000))
    assertThat(parsed!!.deliveryCost).isEqualTo(1000)
  }
}