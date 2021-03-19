package com.catshi.bob.ui

import com.catshi.bob.exceptions.BobTeamAlreadyMatchedException
import com.catshi.bob.exceptions.NeverMatchThemException
import com.catshi.bob.exceptions.NotEnoughBobTicketException
import com.catshi.bob.services.Bob
import com.catshi.bob.types.BobTimeType
import com.catshi.core.types.CityType
import com.catshi.core.utils.TimeHandler
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class BobScheduler(
    val bob: Bob,
    val slackHandler: BobSlackHandler,
) {
    @Scheduled(cron="0 0 1 * * *")
    private fun announceLunchTicketingStarted() {
        slackHandler.postMessageRequest("점심 투표 시작!!!!!")
    }

    @Scheduled(cron="0 0 7 * * *")
    private fun announceDinnerTicketingStarted() {
        slackHandler.postMessageRequest("저녁 투표 시작!!!!!")
    }

    @Scheduled(cron="0 0 2 * * *")
    private fun announceLunchTicketingFinished() {
        slackHandler.postMessageRequest("점심 투표 끝!!!!! 결과는 11시 1분에 납니다")
    }

    @Scheduled(cron="0 0 8 * * *")
    private fun announceDinnerTicketingFinished() {
        slackHandler.postMessageRequest("저녁 투표 끝!!!!! 결과는 5시 1분에 납니다")
    }

    @Scheduled(cron="0 1 2,8 * * *")
    private fun announceTeam() {
        val bobTimeType = when (TimeHandler.nowDateTime().hour) {
            2 -> BobTimeType.LUNCH
            8 -> BobTimeType.DINNER
            else -> return
        }

        val cityList = listOf(CityType.SEOUL, CityType.DAEJEON)
        cityList.forEach { city ->
            try {
                val thisMealBobTeams = bob.determineBobTeam(city, bobTimeType)
                bob.archiveBobTeam(thisMealBobTeams)
                thisMealBobTeams.toString().split("\n").forEach {
                    slackHandler.postMessageRequest(it)
                }
            } catch (e: NotEnoughBobTicketException) {
                slackHandler.postMessageRequest("${city}은 오늘 밥팀이 없어요. 밥들 챙겨 먹고 다녀요~")
            } catch (e: NeverMatchThemException) {
                slackHandler.postMessageRequest("${city}은 오늘 밥팀이 없어요. 밥들 챙겨 먹고 다녀요~")
            } catch (e: BobTeamAlreadyMatchedException) {
                slackHandler.postMessageRequest("")
            }
        }
    }
}