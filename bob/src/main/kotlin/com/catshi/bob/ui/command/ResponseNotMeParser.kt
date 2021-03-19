package com.catshi.bob.ui.command

import org.springframework.stereotype.Component

@Component
class ResponseNotMeParser: CommandParser<ResponseNotMe> {

    private val messages = listOf("ㅇㅁ", "안먹을래유", "안먹", "da")

    override fun fromText(text: String): ResponseNotMe? {
        return if (messages.any { it == text }) ResponseNotMe()
        else null
    }
}