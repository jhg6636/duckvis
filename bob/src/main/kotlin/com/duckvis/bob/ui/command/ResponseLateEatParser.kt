package com.duckvis.bob.ui.command

import org.springframework.stereotype.Component

@Component
class ResponseLateEatParser : CommandParser<ResponseLateEat> {
  private val messages = listOf("!sa", "!ㄴㅁ", "!늦먹", "ㄴㅁ", "sa", "늦먹")

  override fun fromText(text: String): ResponseLateEat? {
    return if (messages.any { it == text }) ResponseLateEat()
    else null
  }
}