package com.duckvis.bob.ui.command

import com.duckvis.core.domain.bob.Menu
import org.springframework.stereotype.Component

@Component
class ResponseRemoveMenuParser : CommandParser<ResponseRemoveMenu> {
  override fun fromText(text: String): ResponseRemoveMenu? {
    if (!text.startsWith("!메뉴제거")) {
      return null
    }

    val removingMenu = text.substringAfter("!메뉴제거").trim()
    return ResponseRemoveMenu(Menu(removingMenu))
  }
}