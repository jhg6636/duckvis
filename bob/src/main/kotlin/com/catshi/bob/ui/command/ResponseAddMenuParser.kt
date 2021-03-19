package com.catshi.bob.ui.command

import com.catshi.bob.domain.Menu
import org.springframework.stereotype.Component

@Component
class ResponseAddMenuParser: CommandParser<ResponseAddMenu> {
    override fun fromText(text: String): ResponseAddMenu? {
        if (!text.startsWith("!메뉴추가")) {
            return null
        }

        val menuName = text.substringAfter("!메뉴추가").trim()
        return ResponseAddMenu(Menu(menuName))
    }
}