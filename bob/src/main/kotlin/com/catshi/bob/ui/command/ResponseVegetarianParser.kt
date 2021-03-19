package com.catshi.bob.ui.command

import org.springframework.stereotype.Component

@Component
class ResponseVegetarianParser: CommandParser<ResponseVegetarian> {

    val messages = listOf("ㅊㅅ", "채식", "ct")

    override fun fromText(text: String): ResponseVegetarian? {
        return if (messages.any { it == text }) ResponseVegetarian()
        else null
    }
}