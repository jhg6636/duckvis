package com.duckvis.nuguri.domain.statistics.service

import com.duckvis.core.SlackConstants
import com.duckvis.core.domain.nuguri.AttendanceCard
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.utils.*
import com.duckvis.nuguri.domain.statistics.dtos.CoreTimeEndResponse
import com.duckvis.nuguri.domain.statistics.dtos.CoreTimeStatisticsResponseDto
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.repository.AttendanceCardNuguriRepository
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDate

/**
 * 어드민의 !코어타임 통계 기능
 */
@Service("CORE_TIME_STATISTICS")
class CoreTimeStatisticsService(
  @Autowired private val attendanceCardNuguriRepository: AttendanceCardNuguriRepository,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val adminStatisticsAction: AdminStatisticsAction,
) : NuguriService {

  companion object {
    val CORE_TIME_DURATION =
      Duration.between(DateTimeMaker.nowDate().coreTimeStart, DateTimeMaker.nowDate().coreTimeEnd).seconds
    const val SLACK_PERCENTAGE = 90
    const val DM_PERCENTAGE = 75
  }

  override val minimumRequestParams: Int
    get() = 0
  override val maximumRequestParams: Int
    get() = 1
  override val minimumPermission: ServicePermission
    get() = ServicePermission.ADMIN


  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    val date = if (serviceRequestDto.params.isEmpty()) {
      DateTimeMaker.nowDate()
    } else {
      DateTimeMaker.stringToDate(serviceRequestDto.params[0])
    }
    return coreTimeStatistics(date).responseString
  }

  @Transactional
  fun coreTimeStatistics(date: LocalDate): CoreTimeStatisticsResponseDto {
    checkNobodyWorked()
    // today
    val today = DateTimeMaker.nowDate()
    val todayString = "${today.year}년 ${today.monthValue}월 ${today.dayOfMonth}일의 출근정보에요~\n\n"

    // 비근무
    val notWorkedUserIds = getNotAttendedUsers()
    val notWorkedString = ":x:오늘의 비근무 멤버:x:\n" + notWorkedUserIds.joinToString("\n") { userId ->
      userRepository.findByIdOrNull(userId)!!.name
    }
    val notWorkedUserCodes = notWorkedUserIds.map { userId ->
      userRepository.findByIdOrNull(userId)?.code ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    }

    // 지각
    val allUserIds = userRepository.findAll()
      .filter { user -> !user.isGone && !user.isBot }
      .map { user -> user.id }
    val notLateUserIds =
      attendanceCardNuguriRepository.getAttendedAt(DateTimeMaker.nowDate().coreTimeStart.plusMinutes(1L))
        .map { attendanceCard ->
          attendanceCard.userId
        }
        .distinct()

    val lateUserIds = allUserIds - notLateUserIds - notWorkedUserIds
    val lateString = ":sweat:오늘의 지각 멤버:sweat:\n" + lateUserIds.joinToString("\n") { userId ->
      "${userRepository.findByIdOrNull(userId)!!.name} 지각(${getAttendTime(userId)} 출근)"
    }

    // 코어타임 미달
    val notEnoughUsersForSlack = getNotEnoughWorkedUsers(SLACK_PERCENTAGE)
      .filter { coreTimeEndResponse ->
        !notWorkedUserCodes.contains(coreTimeEndResponse.userCode)
      }
    val notEnoughUsersForDM = getNotEnoughWorkedUsers(DM_PERCENTAGE)
      .filter { coreTimeEndResponse ->
        !notWorkedUserCodes.contains(coreTimeEndResponse.userCode)
      }
    val notEnoughString = ":stopwatch:오늘의 코어타임 미달 멤버:stopwatch:\n" +
      notEnoughUsersForSlack.joinToString("\n") { coreTimeEndResponse ->
        coreTimeEndResponse.slackString
      }

    return CoreTimeStatisticsResponseDto(
      "$todayString\n$notWorkedString\n$lateString\n$notEnoughString",
      notEnoughUsersForDM
    )
  }

  private fun getNotAttendedUsers(): List<Long> {
    val allUsers = userRepository.findAll()
      .filter { user -> !user.isGone && !user.isBot }
      .map { user -> user.id }
    val attendedUsers = attendanceCardNuguriRepository.getAllCardsBetween(
      DateTimeMaker.nowDateTime().dayStartTime,
      DateTimeMaker.nowDateTime().dayEndTime
    )
      .map { attendanceCard ->
        attendanceCard.userId
      }
      .distinct()

    return allUsers - attendedUsers
  }

  private fun getAttendTime(userId: Long): String {
    val myCoreTimeCards = attendanceCardNuguriRepository.getCoreTimeCards(DateTimeMaker.nowDate())
      .filter { attendanceCard ->
        attendanceCard.isMyCard(userId)
      }
    val firstCard = myCoreTimeCards.minByOrNull { attendanceCard ->
      attendanceCard.loginDateTime
    }

    val firstLoginTime = firstCard?.loginDateTime ?: return "X"

    return firstLoginTime.dateTimeString.split(" ").last()
  }

  private fun getNotEnoughWorkedUsers(percentage: Int): List<CoreTimeEndResponse> {
    val allCards = attendanceCardNuguriRepository.getCoreTimeCards(DateTimeMaker.nowDate())

    // userId -> List<AttendanceCard>
    val userCards: Map<Long, List<AttendanceCard>> = allCards
      .groupBy { attendanceCard -> attendanceCard.userId }

    // (userId, List<AttendanceCard>) -> userId
    return userCards
      .filter { (_, value) ->
        value.sumBy { attendanceCard -> attendanceCard.coreTimeDuration.toInt() } < CORE_TIME_DURATION * percentage / 100
      }
      .map { (key, value) ->
        val user = userRepository.findByIdOrNull(key) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
        CoreTimeEndResponse(
          user.code,
          user.name,
          value.sumBy { attendanceCard -> attendanceCard.coreTimeDuration.toInt() }
        )
      }
  }

  private fun checkNobodyWorked() {
    val allCards = attendanceCardNuguriRepository.getCoreTimeCards(DateTimeMaker.nowDate())
    if (allCards.isEmpty()) {
      throw NuguriException(ExceptionType.NOBODY_HAS_WORKED)
    }
  }

}