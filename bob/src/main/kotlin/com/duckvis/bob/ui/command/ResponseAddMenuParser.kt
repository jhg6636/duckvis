package com.duckvis.bob.ui.command

import com.duckvis.core.domain.bob.Menu
import org.springframework.stereotype.Component

@Component
class ResponseAddMenuParser : CommandParser<ResponseAddMenu> {
  override fun fromText(text: String): ResponseAddMenu? {
    if (!text.startsWith("!메뉴추가")) {
      return null
    }

    val menuName = text.substringAfter("!메뉴추가").trim()
    return ResponseAddMenu(Menu(menuName))
  }
}