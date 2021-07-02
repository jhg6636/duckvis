package com.duckvis.bob.ui.command

import com.duckvis.core.types.shared.CityType
import org.springframework.stereotype.Component

@Component
class ResponseLateEatSpecificPlaceParser : CommandParser<ResponseLateEatSpecificPlace> {
  val messages = listOf("!서울늦먹", "!대전늦먹", "!ㅅㅇㄴㅁ", "!ㄷㅈㄴㅁ", "서울늦먹", "대전늦먹", "ㅅㅇㄴㅁ", "ㄷㅈㄴㅁ")

  override fun fromText(text: String): ResponseLateEatSpecificPlace? {
    if (messages.all { text != it }) {
      return null
    }

    val cityText = when (text.substringBefore("에서")) {
      "!서울", "!ㅅㅇ" -> "서울"
      "!대전", "!ㄷㅈ" -> "대전"
      else -> ""
    }

    val city = CityType.of(cityText)
    return ResponseLateEatSpecificPlace(city)
  }
}