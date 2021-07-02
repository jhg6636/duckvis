package com.duckvis.scheduler.nuguri

import com.duckvis.core.SlackConstants
import com.duckvis.core.SlackConstants.Companion.NUGURI_CHANNEL
import com.duckvis.core.SlackConstants.Companion.NUGURI_CORETIME_CHANNEL
import com.duckvis.core.SlackConstants.Companion.NUGURI_POND_CHANNEL
import com.duckvis.core.domain.nuguri.HolidayRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.utils.*
import com.duckvis.nuguri.domain.statistics.service.CoreTimeStatisticsService
import com.duckvis.nuguri.repository.AttendanceCardNuguriRepository
import com.duckvis.slack.service.PostMessageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class CoreTimeEndScheduler(
  @Autowired private val userRepository: UserRepository,
  @Autowired private val attendanceCardNuguriRepository: AttendanceCardNuguriRepository,
  @Autowired private val holidayRepository: HolidayRepository,
  @Autowired private val postMessageService: PostMessageService,
  @Autowired private val coreTimeStatisticsService: CoreTimeStatisticsService,
) {

  companion object {
    val CORE_TIME_DURATION =
      Duration.between(DateTimeMaker.nowDate().coreTimeStart, DateTimeMaker.nowDate().coreTimeEnd).seconds
    const val SLACK_PERCENTAGE = 90
    const val DM_PERCENTAGE = 75
  }

  @Scheduled(cron = "0 0 8 * * *")
  fun endCoreTime() {
    postMessageService.post("띵띵띵! 오늘의 코어타임 끝! 모두 고생많으셨어요~!!!", NUGURI_CHANNEL)
  }

  @Scheduled(cron = "0 0 8 * * *")
  fun coreTimeStatistics() {
    // today
    val today = DateTimeMaker.nowDate()
    if (holidayRepository.existsByDateEquals(today)) {
      return
    }
    try {
      val coreTimeStatisticsResponse = coreTimeStatisticsService.coreTimeStatistics(today)

      coreTimeStatisticsResponse.notEnoughResponsesForDM.forEach { coreTimeEndResponse ->
        postMessageService.post(coreTimeEndResponse.dmString, coreTimeEndResponse.userCode)
      }

      postMessageService.post(
        coreTimeStatisticsResponse.responseString,
        SlackConstants.NUGURI_CORETIME_CHANNEL
      )
    }
    catch (e: NuguriException) {
      postMessageService.post(
        "오늘 코어타임에 아무도 일하지 않았어요~",
        NUGURI_CORETIME_CHANNEL
      )
      postMessageService.post(
        "<@${NUGURI_CHANNEL}> 오늘 코어타임에 아무도 일하지 않았어요~",
        NUGURI_POND_CHANNEL
      )
    }
  }

}