package com.catshi.bob.ui.command

import org.springframework.stereotype.Component

@Component
class ResponseAllMenuParser: CommandParser<ResponseAllMenu> {
    override fun fromText(text: String): ResponseAllMenu? {
        return if (text == "!메뉴") ResponseAllMenu()
        else null
    }
}