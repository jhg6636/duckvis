package com.duckvis.scheduler.nuguri

import com.duckvis.core.SlackConstants.Companion.NUGURI_POND_CHANNEL
import com.duckvis.core.domain.nuguri.UserProfileRepository
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.core.utils.dateTimeString
import com.duckvis.core.utils.dayEndTime
import com.duckvis.core.utils.dayStartTime
import com.duckvis.nuguri.repository.AttendanceCardNuguriRepository
import com.duckvis.slack.service.PostMessageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.File
import java.nio.file.Paths
import kotlin.random.Random

@Component
class EveryDayScheduler(
  @Autowired private val attendanceCardNuguriRepository: AttendanceCardNuguriRepository,
  @Autowired private val userProfileRepository: UserProfileRepository,
  @Autowired private val postMessageService: PostMessageService,
) {

  /**
   * 생일축하방송
   */
  @Scheduled(cron = "0 0 0 * * *")
  fun happyBirthday() {
    val allProfiles = userProfileRepository.findAll()
    val birthdayProfiles = allProfiles.filter { userProfile ->
      userProfile.isBirthday(DateTimeMaker.nowDate())
    }
    if (birthdayProfiles.isEmpty()) {
      return
    }
    postMessageService.post(
      "오늘은 ${DateTimeMaker.nowDate().monthValue}월 ${DateTimeMaker.nowDate().dayOfMonth}일이에요~",
      NUGURI_POND_CHANNEL
    )
    birthdayProfiles.forEach { userProfile ->
      val message = userProfile.birthdayString
      postMessageService.post(message, NUGURI_POND_CHANNEL)
      Thread.sleep(Random.nextLong(500, 1000))
    }
    postMessageService.post("그럼 오늘도 다들 힘차게 화이팅이에요~~!", NUGURI_POND_CHANNEL)
  }

  /**
   * 매일 백업
   */
  @Scheduled(cron = "0 59 20 * * *")
  fun backUpDaily() {
    val dateTime = DateTimeMaker.nowDateTime()
    val fileName = "${dateTime.dateTimeString.split(" ").first().replace("-", "_")}.csv"
    val allCards = attendanceCardNuguriRepository.getAllCardsBetween(dateTime.dayStartTime, dateTime.dayEndTime)
    val path = Paths.get("").toAbsolutePath().toString()
    val file = File("$path/daily/$fileName")
    file.createNewFile()
    file.writeText("유저ID,프로젝트ID,서브플젝ID,근무vs실수,출근일시,퇴근일시,일한시간,야간,휴일,연장\n")
    allCards.forEach { card ->
      file.appendText(card.backUpCsvString)
      file.appendText("\n")
    }
  }

}