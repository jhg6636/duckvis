package com.catshi.bob.ui

import com.duckvis.bob.types.StatisticsOption
import com.duckvis.bob.ui.BobSlackHandler
import com.duckvis.bob.ui.command.ResponseHelp
import com.duckvis.bob.ui.command.ResponseMe
import com.duckvis.bob.ui.command.ResponseStatistics
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class BobSlackHandlerTest constructor (
    @Autowired private val bobSlackHandler: BobSlackHandler,
) {
    @Test
    fun `저요`() {
        // given

        // when
        val bobCommand = bobSlackHandler.textToCommand("ㅈㅇ")

        // then
        assertThat(bobCommand).hasSameClassAs(ResponseMe())
    }

    @Test
    fun `!사용법`() {
        // given

        // when
        val bobCommand = bobSlackHandler.textToCommand("!사용법")

        // then
        assertThat(bobCommand).hasSameClassAs(ResponseHelp())
    }

    @Test
    fun `밥통계`() {
        // given

        // when
        val bobCommand = bobSlackHandler.textToCommand("ㅂㅌㄱ")

        // then
        assertThat(bobCommand).hasSameClassAs(ResponseStatistics(StatisticsOption.ALL))
    }
}