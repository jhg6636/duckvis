package com.duckvis.bob.ui.command

import com.duckvis.core.types.shared.CityType
import org.springframework.stereotype.Component

@Component
class ResponseVegetarianSpecificPlaceParser : CommandParser<ResponseVegetarianSpecificPlace> {
  val messages = listOf("서울채식", "대전채식", "ㅅㅇㅊㅅ", "ㄷㅈㅊㅅ")

  override fun fromText(text: String): ResponseVegetarianSpecificPlace? {
    if (messages.all { text != it }) {
      return null
    }

    println(text.substring(0, 2))
    val city = CityType.of(text.substring(0, 2))
    return ResponseVegetarianSpecificPlace(city)

  }
}