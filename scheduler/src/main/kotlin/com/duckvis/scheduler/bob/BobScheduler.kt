package com.duckvis.scheduler.bob

import com.duckvis.bob.services.*
import com.duckvis.core.SlackConstants
import com.duckvis.core.SlackConstants.Companion.BOB_CHANNEL
import com.duckvis.core.domain.bob.BobHistoryQueryDslRepository
import com.duckvis.core.domain.bob.UserPayQueryDslRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.bob.BobTeamAlreadyMatchedException
import com.duckvis.core.exceptions.bob.NeverMatchThemException
import com.duckvis.core.exceptions.bob.NoSuchUserException
import com.duckvis.core.exceptions.bob.NotEnoughBobTicketException
import com.duckvis.core.types.bob.BobTimeType
import com.duckvis.core.types.shared.CityType
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.core.utils.isWeekend
import com.duckvis.slack.service.PostMessageService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import kotlin.random.Random

@Component
class BobScheduler(
  private val bobTeamService: BobTeamService,
  private val bobMoneyService: BobMoneyService,
  private val bobHistoryQueryDslRepository: BobHistoryQueryDslRepository,
  private val userRepository: UserRepository,
  private val userPayQueryDslRepository: UserPayQueryDslRepository,
  private val postMessageService: PostMessageService,
) {
  @Scheduled(cron = "0 0 1 * * *")
  fun announceLunchTicketingStarted() {
    postMessageService.post("점심 투표 시작!!!!!", BOB_CHANNEL)
  }

  @Scheduled(cron = "0 0 7 * * *")
  fun announceDinnerTicketingStarted() {
    postMessageService.post("저녁 투표 시작!!!!!", BOB_CHANNEL)
  }

  @Scheduled(cron = "0 0 2 * * *")
  fun announceLunchTicketingFinished() {
    postMessageService.post("점심 투표 끝!!!!! 결과는 11시 1분에 납니다", BOB_CHANNEL)
  }

  @Scheduled(cron = "0 0 8 * * *")
  fun announceDinnerTicketingFinished() {
    postMessageService.post("저녁 투표 끝!!!!! 결과는 5시 1분에 납니다", BOB_CHANNEL)
  }

  @Scheduled(cron = "0 1 2,8 * * *")
  fun announceTeam() {
    val bobTimeType = when (DateTimeMaker.nowDateTime().hour) {
      2 -> BobTimeType.LUNCH
      8 -> BobTimeType.DINNER
      else -> return
    }

    val cityList = listOf(CityType.SEOUL, CityType.DAEJEON)
    cityList.forEach { city ->
      try {
        val decisionLogic = when {
          city == CityType.DAEJEON -> DaejeonTeamDecisionLogic()
          DateTimeMaker.nowDate().isWeekend -> WeekendTeamDecisionLogic()
          else -> CovidTeamDecisionLogic()
        }
        bobTeamService.determineBobTeam(city, bobTimeType, decisionLogic).teams.forEach { thisMealBobTeams ->
          thisMealBobTeams.asString.split("\n").forEach {
            postMessageService.post(it, BOB_CHANNEL)
            Thread.sleep(Random.nextLong(500, 1000))
          }
        }
      } catch (e: NotEnoughBobTicketException) { // TODO 뭔가 있을것같다....
        postMessageService.post("${city}은 오늘 밥팀이 없어요. 밥들 챙겨 먹고 다녀요~", BOB_CHANNEL)
      } catch (e: NeverMatchThemException) {
        postMessageService.post("${city}은 오늘 밥팀이 없어요. 밥들 챙겨 먹고 다녀요~", BOB_CHANNEL)
      } catch (e: BobTeamAlreadyMatchedException) {
        postMessageService.post("", BOB_CHANNEL)
      }
    }
  }

  @Scheduled(cron = "0 20 3,9 * * *")
  @Transactional
  fun askPayType() {
    val bobTimeType = when (DateTimeMaker.nowDateTime().hour) {
      3 -> BobTimeType.LUNCH
      9 -> BobTimeType.DINNER
      else -> BobTimeType.BREAKFAST
    }
    val unRecordedUsers = mutableListOf<Long>()
    val thisMealEaterIds = bobHistoryQueryDslRepository.getThisMealUserIds(bobTimeType)
    thisMealEaterIds.forEach { userId ->
      userPayQueryDslRepository.thisMealUserPay(userId, bobTimeType) ?: unRecordedUsers.add(userId)
    }
    if (unRecordedUsers.isNotEmpty()) {
      val unRecordedUsersString =
        unRecordedUsers.joinToString(",") { userId -> userId.userIdToTagString }
      val message = ":alert::alert::alert:\n아직 금액을 입력하지 않은 ${unRecordedUsersString}님들은 " +
        "${bobTimeType.endHour + 13}시 전까지 입력해 주세요옹~"

      postMessageService.post(message, BOB_CHANNEL)
    }
  }

  @Scheduled(cron = "0 0 3 * * *")
  fun alertMinus() {
    val allUsers = userRepository.findAll()
    val minusUsers = allUsers.filter { user ->
      bobMoneyService.responseTotalOverMoney(user.id) < 0
    }
    if (minusUsers.isNotEmpty()) {
      val minusUsersString = minusUsers.joinToString("\n") { user ->
        val recentPays = userPayQueryDslRepository.getMyRecentPays(user.id)
        ":pushpin:${user.name} 최근 내역(3회) : ${recentPays.joinToString(", ") { pay -> pay.payString }}"
      }
      postMessageService.post(
        "초과금이 마이너스인 사람들의 목록입니다\n\n$minusUsersString",
        userRepository.findByName(SlackConstants.BOB_MANAGER)!!.code
      )
    }
  }

  private val Long.userIdToTagString: String
    get() = userRepository.findByIdOrNull(this)?.tagString ?: throw NoSuchUserException()
}