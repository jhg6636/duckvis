package com.duckvis.bob.ui.command

import org.springframework.stereotype.Component

@Component
class ResponseHelpParser : CommandParser<ResponseHelp> {
  override fun fromText(text: String): ResponseHelp? {
    return if (text == "!사용법") ResponseHelp()
    else null
  }
}