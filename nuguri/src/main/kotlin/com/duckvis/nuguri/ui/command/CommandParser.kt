package com.duckvis.nuguri.ui.command

import com.duckvis.core.types.UserLevelType

interface CommandParser {
    fun fromText(text: String): NuguriCommand

    val minimumUserLevel: UserLevelType
}