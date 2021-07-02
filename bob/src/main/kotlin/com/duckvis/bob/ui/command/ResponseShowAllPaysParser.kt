package com.duckvis.bob.ui.command

class ResponseShowAllPaysParser : CommandParser<ResponseShowAllPays> {

  override fun fromText(text: String): ResponseShowAllPays? {
    val splitText = text.split(" ")
    if (splitText.size == 2 && splitText[0] == "!금액확인") {
      return ResponseShowAllPays(splitText[1], 20)
    }
    else if (splitText.size == 3 && splitText[0] == "!금액확인") {
      return ResponseShowAllPays(splitText[1], splitText[2].toInt())
    }
    return null
  }
}