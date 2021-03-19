package com.catshi.bob.ui.command

import com.catshi.core.types.CityType
import org.springframework.stereotype.Component

@Component
class ResponseMeSpecificPlaceParser: CommandParser<ResponseMeSpecificPlace> {
    val messages = listOf("서울에서", "대전에서")

    override fun fromText(text: String): ResponseMeSpecificPlace? {
        if (messages.all { it != text }) {
            return null
        }

        val city = CityType.of(text.substringBefore("에서"))
        return ResponseMeSpecificPlace(city)
    }
}