package com.duckvis.bob.ui.command

import org.springframework.stereotype.Component

@Component
interface CommandParser<T : BobCommand> {

    fun fromText(text: String): T?

}
