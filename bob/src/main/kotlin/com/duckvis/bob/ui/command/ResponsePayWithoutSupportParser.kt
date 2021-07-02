package com.duckvis.bob.ui.command

import org.springframework.stereotype.Component

@Component
class ResponsePayWithoutSupportParser : CommandParser<ResponsePayWithoutSupport> {
  override fun fromText(text: String): ResponsePayWithoutSupport? {
    val splitText = text.split(" ")
    if (splitText[0] == "!금액" && splitText.size == 3 && splitText[2] == "%미지원") {
      return ResponsePayWithoutSupport(splitText[1].toInt())
    }
    return null
  }
}