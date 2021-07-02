package com.duckvis.bob.ui.command

import org.springframework.stereotype.Component

@Component
class ResponseSelfPayWithSupportParser : CommandParser<ResponseSelfPayWithSupport> {
  override fun fromText(text: String): ResponseSelfPayWithSupport? {
    val splitText = text.split(" ")
    if (splitText.size == 3 && splitText[0] == ("!금액") && splitText[2] == "%개인결제") {
      return ResponseSelfPayWithSupport(0)
    } else if (splitText.size == 2 && splitText[0] == "!배달비") {
      return ResponseSelfPayWithSupport(splitText[1].toInt())
    }
    return null
  }
}