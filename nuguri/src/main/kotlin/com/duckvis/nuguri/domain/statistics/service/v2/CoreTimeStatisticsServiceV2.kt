package com.duckvis.nuguri.domain.statistics.service.v2

import com.duckvis.core.domain.nuguri.AttendanceCard
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.statistics.NuguriCoreTimeStatisticsRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.core.utils.*
import com.duckvis.nuguri.domain.statistics.dtos.CoreTimeEndResponse
import com.duckvis.nuguri.domain.statistics.dtos.CoreTimeStatisticsResponseDto
import com.duckvis.nuguri.domain.statistics.service.CoreTimeStatisticsService
import com.duckvis.nuguri.repository.AttendanceCardNuguriRepository
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDate

@Service("CORE_TIME_STATISTICS_V2")
class CoreTimeStatisticsServiceV2(
  private val attendanceCardNuguriRepository: AttendanceCardNuguriRepository,
  private val userRepository: UserRepository,
  private val adminStatisticsActionV2: AdminStatisticsActionV2,
) : NuguriServiceV2 {

  companion object {
    val CORE_TIME_DURATION =
      Duration.between(DateTimeMaker.nowDate().coreTimeStart, DateTimeMaker.nowDate().coreTimeEnd).seconds
    const val SLACK_PERCENTAGE = 90
    const val DM_PERCENTAGE = 75
  }

  override val type: CommandMinorType
    get() = CommandMinorType.CORE_TIME_STATISTICS

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriCoreTimeStatisticsRequestParameterDto
    return coreTimeStatistics(params.date).responseString
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
    val notEnoughUsersForSlack = getNotEnoughWorkedUsers(CoreTimeStatisticsService.SLACK_PERCENTAGE)
      .filter { coreTimeEndResponse ->
        !notWorkedUserCodes.contains(coreTimeEndResponse.userCode)
      }
    val notEnoughUsersForDM = getNotEnoughWorkedUsers(CoreTimeStatisticsService.DM_PERCENTAGE)
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
        value.sumBy { attendanceCard -> attendanceCard.coreTimeDuration.toInt() } < CoreTimeStatisticsService.CORE_TIME_DURATION * percentage / 100
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