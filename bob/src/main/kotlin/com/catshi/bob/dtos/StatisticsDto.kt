package com.catshi.bob.dtos

import com.catshi.bob.types.StatisticsOption
import com.catshi.core.domain.User


class StatisticsDto(
    private val userName: String,
    val leaderCount: Long,
    val freqeuntTeammateName: String,
    val frequency: Int,
    private val option: StatisticsOption,
) {
    override fun toString(): String {
        return when (option) {
            StatisticsOption.ALL -> "${userName}님은 이번 달에 밥부장을 ${leaderCount}번 하셨어요 너무 예쁘다\n" +
                "${userName}님의 이번 달 영혼의 짝꿍은 ${freqeuntTeammateName}님이에요 ${frequency}번이나 같이 드셨네요"
            StatisticsOption.BOB_LEADER -> "${userName}님은 이번 달에 밥부장을 ${leaderCount}번 하셨어요 너무 예쁘다"
            StatisticsOption.TEAMMATE -> "${userName}님의 이번 달 영혼의 짝꿍은 ${freqeuntTeammateName}님이에요 ${frequency}번이나 같이 드셨네요"
        }
    }
}