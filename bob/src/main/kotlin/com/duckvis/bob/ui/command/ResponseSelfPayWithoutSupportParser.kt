package com.duckvis.bob.ui.command

import org.springframework.stereotype.Service

@Service
class ResponseSelfPayWithoutSupportParser : CommandParser<ResponseSelfPayWithoutSupport> {

  override fun fromText(text: String): ResponseSelfPayWithoutSupport? {
    val splitText = text.split(" ")
    if (splitText.size == 3 && splitText[0] == "!배달비" && splitText[2] == "%미지원") {
      return ResponseSelfPayWithoutSupport(splitText[1].toInt())
    }
    return null
  }

}