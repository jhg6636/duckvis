package com.duckvis.nuguri.utils

import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows

class AssertNuguriException(
  private val type: ExceptionType
) {
  fun assert(lambda: () -> Unit) {
    val type = assertThrows<NuguriException>(lambda).type
    assertThat(type).isEqualTo(this.type)
  }
}