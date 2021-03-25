package com.duckvis.bob.ui.command

import com.duckvis.bob.types.StatisticsOption
import org.springframework.stereotype.Component

@Component
class ResponseStatisticsParser : CommandParser<ResponseStatistics> {
    val messages = listOf("밥통계", "ㅂㅌㄱ", "qxr")

    override fun fromText(text: String): ResponseStatistics? {
        if (messages.all { !text.startsWith(it) }) {
            return null
        }
        if (text.split(" ").size == 1) {
            return ResponseStatistics(StatisticsOption.ALL)
        }
        val optionText = text.split(" ")[1]

        when (text.split(" ")[1].substringAfter("%")) {
            "밥부장" -> return ResponseStatistics(StatisticsOption.BOB_LEADER)
            "짝꿍" -> return ResponseStatistics(StatisticsOption.TEAMMATE)
            else -> return null
        }

    }
}