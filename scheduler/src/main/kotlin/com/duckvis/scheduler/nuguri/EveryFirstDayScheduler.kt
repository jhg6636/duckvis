package com.duckvis.scheduler.nuguri

import com.duckvis.core.SlackConstants
import com.duckvis.core.domain.nuguri.*
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.Mistake
import com.duckvis.core.dtos.nuguri.WorkTypeDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.HolidayType
import com.duckvis.core.types.nuguri.SpecialStatisticsType
import com.duckvis.core.utils.*
import com.duckvis.nuguri.domain.admin.service.holiday.ThisMonthInfoService
import com.duckvis.nuguri.domain.admin.service.user.SetDayOffService
import com.duckvis.nuguri.domain.admin.service.user.SetSickDayOffService
import com.duckvis.nuguri.domain.statistics.dtos.AdminStatisticsRequestDto
import com.duckvis.nuguri.domain.statistics.dtos.StatisticsRequestDto
import com.duckvis.nuguri.domain.statistics.service.NormalStatisticsAction
import com.duckvis.nuguri.external.mail.NuguriMailSender
import com.duckvis.nuguri.repository.AttendanceCardNuguriRepository
import com.duckvis.slack.service.PostMessageService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.nio.file.Paths
import java.time.LocalDate
import java.time.Month
import java.time.Year

@Component
class EveryFirstDayScheduler(
  @Autowired private val holidayRepository: HolidayRepository,
  @Autowired private val thisMonthInfoService: ThisMonthInfoService,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val setDayOffService: SetDayOffService,
  @Autowired private val setSickDayOffService: SetSickDayOffService,
  @Autowired private val normalStatisticsAction: NormalStatisticsAction,
  @Autowired private val attendanceCardNuguriRepository: AttendanceCardNuguriRepository,
  @Autowired private val userProfileRepository: UserProfileRepository,
  @Autowired private val attendanceCardRepository: AttendanceCardRepository,
  @Autowired private val projectRepository: ProjectRepository,
  @Autowired private val nuguriMailSender: NuguriMailSender,
  @Autowired private val postMessageService: PostMessageService,
) {

  private val log = LoggerFactory.getLogger(this.javaClass)

  /**
   * 양력 법정 공휴일은 1월 1일에 미리 holiday 등록
   */
  @Scheduled(cron = "0 0 0 1 1 *")
  fun saveAllNationalHolidays() {
    val year = DateTimeMaker.nowDate().year
    val nationalHolidays = listOf(
      LocalDate.of(year, 1, 1),
      LocalDate.of(year, 3, 1),
      LocalDate.of(year, 5, 5),
      LocalDate.of(year, 6, 6),
      LocalDate.of(year, 8, 15),
      LocalDate.of(year, 10, 3),
      LocalDate.of(year, 10, 9),
      LocalDate.of(year, 12, 25)
    )
    nationalHolidays.forEach {
      holidayRepository.save(Holiday(it, HolidayType.NATIONAL))
    }
  }

  /**
   * 유저 근무목표 자동 설정 및 휴가/병가 리셋
   */
  @Scheduled(cron = "0 1 0 1 * *")
  @Transactional
  fun resetUserProfiles() {
    val thisMonthWorkSeconds = thisMonthInfoService.thisMonthWorkingSeconds(DateTimeMaker.nowDateTime().monthValue, DateTimeMaker.nowDate().year)
    log.info("thisMonth({})WorkSeconds {}", DateTimeMaker.nowDate().month, thisMonthWorkSeconds)
    log.info("userProfiles: {}", userProfileRepository.findAll().size)
    userProfileRepository.findAll().forEach { userProfile ->
      userProfile.changeTargetWorkSeconds(thisMonthWorkSeconds)
      val user = userRepository.findByIdOrNull(userProfile.userId) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
      setDayOffService.setDayOff(user.name, 0)
      setSickDayOffService.setDayOffSick(user.name, 0)
    }
    postMessageService.post(
      "오늘은 ${DateTimeMaker.nowDate().monthValue}월 1일이에요~\n이번달 모두의 근무목표를 " +
        "${thisMonthWorkSeconds.secondsToString}로 설정해두었어요~\n그럼 오늘도 다들 힘차게 화이팅이에요~~!",
      SlackConstants.NUGURI_POND_CHANNEL
    )
  }

  /**
   * 주말 holiday 자동 지정
   */
  @Scheduled(cron = "0 0 0 1 * *")
  @Transactional
  fun setWeekends() {
    val year = DateTimeMaker.nowDate().year
    val month = DateTimeMaker.nowDate().monthValue
    for (day in 1..Month.of(month).length(Year.isLeap(year.toLong()))) {
      val date = LocalDate.of(year, month, day)
      if (date.isWeekend) {
        val beforeRegisteredHoliday = holidayRepository.findByDateEquals(date)
        beforeRegisteredHoliday?.setWeekend()
          ?: holidayRepository.save(Holiday(date, HolidayType.WEEKEND))
      }
    }
  }

  /**
   * 이전달 것 백업
   */
  @Scheduled(cron = "0 10 0 1 * *")
  fun backUpMonthly() {
    val dateTime = DateTimeMaker.nowDateTime().minusDays(1)
    val fileName = "${dateTime.year}_${dateTime.month.name}.csv"
    val allCards = attendanceCardNuguriRepository.getAllCardsBetween(dateTime.monthStartTime, dateTime.monthEndTime)
    val path = Paths.get("").toAbsolutePath().toString()

    val csvFile = File("$path/$fileName")
    allCards.forEach { card ->
      csvFile.appendText(card.backUpCsvString)
      csvFile.appendText("\n")
    }

    nuguriMailSender.send("mercury@selectstar.ai", fileName.split(".").first(), "", csvFile)

    // daily 백업 삭제
    File("$path/daily").walk()
      .maxDepth(2)
      .filter { file ->
        file.path.endsWith(".csv")
      }
      .forEach { file ->
        file.delete()
      }
  }

  /**
   * 이전 달 통계 모두에게 전송
   */
  @Scheduled(cron = "0 30 0 1 * *")
  fun sendMonthlyStatistics() {
    val users = userRepository.findAll()
      .filter { user ->
        !user.isBot && !user.isGone
      }
    users.forEach { user ->
      val message = normalStatisticsAction.act(
        StatisticsRequestDto(
          user.code,
          null,
          false,
          SpecialStatisticsType.MONTHLY,
          DateTimeMaker.nowDateTime().minusMonths(1).monthStartTime,
          DateTimeMaker.nowDateTime().minusMonths(1).monthEndTime,
          WorkTypeDto(),
          null,
          AdminStatisticsRequestDto()
        )
      )

      postMessageService.post(
        "지난 달 통계입니다~\n\n" + message
          .substringBefore("\n이번 달 근무시간까지 ")
          .substringAfter("어디 봅시다...\n이번달 통계에요~\n"),
        user.code
      )
    }
  }

  /**
   * 기본 근무, 연장 근무 시간 조정
   */
  @Scheduled(cron = "0 0 0 1 * *")
  @Transactional
  fun modifyExtendFaults() {
    val extendedUserIds = attendanceCardNuguriRepository.getAllCardsBetween(
      DateTimeMaker.nowDateTime().minusMonths(1).monthStartTime,
      DateTimeMaker.nowDateTime().minusMonths(1).monthEndTime
    )
      .filter { card -> card.isSameWorkType(WorkTypeDto(isExtended = true)) }
      .map { card -> card.userId }
      .distinct()

    extendedUserIds.forEach { userId ->
      val allCards = attendanceCardNuguriRepository.getMyCardsBetween(
        userId,
        DateTimeMaker.nowDateTime().minusMonths(1).monthStartTime,
        DateTimeMaker.nowDateTime().minusMonths(1).monthEndTime,
      )

      val normalWorkTime = allCards
        .filter { card -> !card.isExtended }
        .sumBy { card -> card.durationSeconds ?: 0 }

      val extendedWorkTime = allCards
        .filter { card -> card.isExtended }
        .sumBy { card -> card.durationSeconds ?: 0 }

      val userProfile = userProfileRepository.findByUserId(userId) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
      if (normalWorkTime < userProfile.targetWorkSeconds) {
        val modifySeconds = kotlin.math.min(userProfile.targetWorkSeconds - normalWorkTime, extendedWorkTime)
        val minusMistake = Mistake(
          userId,
          projectRepository.findByNameOrNickname("월말수정", "월말수정")?.id
            ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT),
          null,
          -modifySeconds,
          WorkTypeDto(isExtended = true)
        )
        val plusMistake = Mistake(
          userId,
          projectRepository.findByNameOrNickname("월말수정", "월말수정")?.id
            ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT),
          null,
          modifySeconds,
          WorkTypeDto()
        )
        attendanceCardRepository.saveAll(
          listOf(
            AttendanceCard(minusMistake, DateTimeMaker.nowDateTime().minusHours(12)),
            AttendanceCard(plusMistake, DateTimeMaker.nowDateTime().minusHours(12))
          )
        )
        val user = userRepository.findByIdOrNull(userId) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
        postMessageService.post("${modifySeconds.secondsToString} 만큼의 시간이 연장에서 기본 근무로 조정되었어요~", user.code)
      }
    }
  }

}