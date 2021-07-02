package com.duckvis.bob.ui.command

import org.springframework.stereotype.Component

@Component
class ResponseMeParser : CommandParser<ResponseMe> {

  private val messages = listOf("wd", "ㅈㅇ", "저요!!!!", "저요")

  override fun fromText(text: String): ResponseMe? {
    return if (messages.any { it == text }) ResponseMe()
    else null
  }

}
