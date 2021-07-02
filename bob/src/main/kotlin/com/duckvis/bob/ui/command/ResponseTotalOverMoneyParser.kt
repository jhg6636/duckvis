package com.duckvis.bob.ui.command

import org.springframework.stereotype.Component

@Component
class ResponseTotalOverMoneyParser : CommandParser<ResponseTotalOverMoney> {
  override fun fromText(text: String): ResponseTotalOverMoney? {
    val commandList = listOf("!내초과금", "!초과금", "ㅊㄱㄱ", "ㅊㄲ", "!ㅊㄲ", "!ㅊㄱㄱ", "crr", "Crr", "!crr")
    if (commandList.contains(text)) {
      return ResponseTotalOverMoney()
    }
    return null
  }
}