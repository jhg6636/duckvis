package com.duckvis.bob.ui.command

import com.duckvis.core.types.shared.CityType
import org.springframework.stereotype.Component

@Component
class ResponseMeSpecificPlaceParser : CommandParser<ResponseMeSpecificPlace> {
  val messages = listOf("서울에서", "대전에서", "ㄷㅈㅇㅅ", "ㅅㅇㅇㅅ")

  override fun fromText(text: String): ResponseMeSpecificPlace? {
    if (messages.all { it != text }) {
      return null
    }

    val city = CityType.of(text.substring(0, 2))
    return ResponseMeSpecificPlace(city)
  }
}