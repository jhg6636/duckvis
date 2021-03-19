package com.catshi.bob.ui.command

import com.catshi.core.types.CityType
import org.springframework.stereotype.Component

@Component
class ChangeLivingCityParser : CommandParser<ResponseChangeLivingCity> {

    override fun fromText(text: String): ResponseChangeLivingCity? {
        if (!text.startsWith("이제 ") || !text.endsWith(" 살래")) return null
        val cityString = text.split(" ")[1]
        val city = CityType.valueOf(cityString)

        return ResponseChangeLivingCity(city)
    }

}