package com.duckvis.bob.ui.command

import com.duckvis.bob.ui.command.CommandParser
import com.duckvis.bob.ui.command.ResponseAllMenu
import org.springframework.stereotype.Component

@Component
class ResponseAllMenuParser : CommandParser<ResponseAllMenu> {
    override fun fromText(text: String): ResponseAllMenu? {
        return if (text == "!메뉴") ResponseAllMenu()
        else null
    }
}