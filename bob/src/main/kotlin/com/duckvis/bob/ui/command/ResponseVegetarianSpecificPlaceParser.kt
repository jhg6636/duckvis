package com.duckvis.bob.ui.command

import com.duckvis.core.types.CityType
import org.springframework.stereotype.Component

@Component
class ResponseVegetarianSpecificPlaceParser : CommandParser<ResponseVegetarianSpecificPlace> {
    val messages = listOf("서울채식", "대전채식", "ㅅㅇㅊㅅ", "ㄷㅈㅊㅅ")

    override fun fromText(text: String): ResponseVegetarianSpecificPlace? {
        if (messages.all { text != it }) {
            return null
        }

        val cityText = when (text.substringBefore("에서")) {
            "서울", "ㅅㅇ" -> "서울"
            "대전", "ㄷㅈ" -> "대전"
            else -> ""
        }

        val city = CityType.of(cityText)
        return ResponseVegetarianSpecificPlace(city)
    }
}