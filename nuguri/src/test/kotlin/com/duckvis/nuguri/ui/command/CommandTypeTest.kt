package com.duckvis.nuguri.ui.command

import com.duckvis.nuguri.ui.CommandType
import org.junit.jupiter.api.Test

class CommandTypeTest {
  @Test
  fun x() {
    println(CommandType.of("!ㅇㄱㅌㄱ"))
  }
}