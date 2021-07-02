package com.duckvis.bob.ui.command

import org.springframework.stereotype.Component

@Component
class ResponsePayWithSupportParser : CommandParser<ResponsePayWithSupport> {
  override fun fromText(text: String): ResponsePayWithSupport? {
    val splitText = text.split(" ")
    if ((splitText.size == 2 && splitText[0] == "!금액") || (splitText[0] == "!금액" && splitText.size == 3 && splitText[2] == "%지원")) {
      return ResponsePayWithSupport(splitText[1].toInt())
    }
    return null
  }
}