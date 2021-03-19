package com.catshi.bob.dtos

import com.catshi.bob.domain.Menu

class MenuList(
    private val menus: List<Menu>,
) {

    override fun toString(): String {
        return "모든 메뉴 여기있습니다~\n" +
                menus.joinToString(separator = ", ") { it.name }
    }

}
