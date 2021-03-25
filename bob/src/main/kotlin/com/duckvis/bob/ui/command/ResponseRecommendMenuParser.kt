package com.duckvis.bob.ui.command

import com.duckvis.bob.domain.Menu
import org.springframework.stereotype.Component

@Component
class ResponseRecommendMenuParser : CommandParser<ResponseRecommendMenu> {
    val messages = listOf("ㅊㅊ", "메뉴추천", "cc")

    override fun fromText(text: String): ResponseRecommendMenu? {
        if (messages.all { text.split(" ")[0] != it }) {
            return null
        }

        if (text.split(" ").size <= 2) {
            return ResponseRecommendMenu(emptyList())
        }

        if (text.split(" ").size > 3 || text.split(" ")[1] != "%제외") {
            return null
        }

        val exceptingMenus = text.split(" ")[2].split(",").map { Menu(it) }
        return ResponseRecommendMenu(exceptingMenus)
    }
}