package com.duckvis.nuguri.domain.admin.service.holiday

import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.utils.*
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.repository.HolidayNuguriRepository
import com.duckvis.nuguri.repository.UserDayOffNuguriRepository
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import com.querydsl.core.Tuple
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * !이번달, !달정보 기능
 */
@Service("THIS_MONTH_INFO")
class ThisMonthInfoService(
  @Autowired private val holidayNuguriRepository: HolidayNuguriRepository,
  @Autowired private val userDayOffNuguriRepository: UserDayOffNuguriRepository,
  @Autowired private val userRepository: UserRepository,
) : NuguriService {

  override val minimumRequestParams: Int
    get() = 0
  override val maximumRequestParams: Int
    get() = 2
  override val minimumPermission: ServicePermission
    get() = ServicePermission.ADMIN

  override fun checkTypo(serviceRequestDto: ServiceRequestDto) {
    super.checkTypo(serviceRequestDto)
    if (serviceRequestDto.text.contains("이번") && serviceRequestDto.params.isNotEmpty()) {
      throw NuguriException(ExceptionType.TYPO)
    }
    if (serviceRequestDto.params.size == 1 && (serviceRequestDto.params[0].any { !it.isDigit() } || serviceRequestDto.params[0].toInt() > 12)) {
      throw NuguriException(ExceptionType.MONTH_TYPO)
    }
  }

  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    val thisMonth = when (serviceRequestDto.params.size) {
      0 -> DateTimeMaker.nowDate().monthValue
      1, 2 -> serviceRequestDto.params[0].toInt()
      else -> throw NuguriException(ExceptionType.TYPO)
    }
    val year = when (serviceRequestDto.params.size) {
      0, 1 -> DateTimeMaker.nowDate().year
      2 -> serviceRequestDto.params[1].toInt()
      else -> throw NuguriException(ExceptionType.TYPO)
    }
    val offUsers = thisMonthOffUsers(thisMonth, year)
    val sickOffUsers = thisMonthSickOffUsers(thisMonth, year)

    return "${thisMonth}월의 달 정보에요~\n" +
      ":calendar:근무일: ${thisMonthWorkingDays(thisMonth, year)}일\n" +
      ":clock9:근무목표: ${thisMonthWorkingSeconds(thisMonth, year).secondsToString}\n\n" +
      "이 달의 휴가 사용 내역이에요~\n\n" +
      offUsers.joinToString("\n") { tuple ->
        val userName = userRepository.findByIdOrNull(tuple.get(0, Long::class.java))
          ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
        "$userName ${tuple.get(1, Int::class.java)}일 사용"
      } +
      "\n\n이 달의 병가 사용 내역이에요~\n\n" +
      sickOffUsers.joinToString("\n") { tuple ->
        val userName = userRepository.findByIdOrNull(tuple.get(0, Long::class.java))
          ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
        "$userName ${tuple.get(1, Int::class.java)}일 사용"
      }
  }

  @Transactional
  fun thisMonthWorkingDays(month: Int, year: Int): Int {
    if (month > 12 || month < 1) {
      throw NuguriException(ExceptionType.MONTH_TYPO)
    }
    val monthLength = DateTimeMaker.nowDate().withYear(year).withMonth(month).lengthOfMonth()
    return monthLength -
      holidayNuguriRepository.getHolidays(
        DateTimeMaker.nowDate().withYear(year).withMonth(month).withDayOfMonth(1),
        DateTimeMaker.nowDate().withYear(year).withMonth(month).withDayOfMonth(monthLength)
      )
        .size
  }

  @Transactional
  fun thisMonthWorkingSeconds(month: Int, year: Int): Int {
    if (month > 12 || month < 1) {
      throw NuguriException(ExceptionType.MONTH_TYPO)
    }
    val monthLength = DateTimeMaker.nowDate().withYear(year).withMonth(month).lengthOfMonth()
    val holidaysExceptWeekend = holidayNuguriRepository.getHolidays(
      DateTimeMaker.nowDate().withYear(year).withMonth(month).withDayOfMonth(1),
      DateTimeMaker.nowDate().withYear(year).withMonth(month).withDayOfMonth(monthLength)
    )
      .filter { holiday -> !holiday.isWeekend }

    val hours: Double = (monthLength / 7.0 * 40.0) - (holidaysExceptWeekend.size * 8.0)

    return (hours * 3600).toInt()
  }

  @Transactional
  fun thisMonthOffUsers(month: Int, year: Int): List<Tuple> {
    if (month > 12 || month < 1) {
      throw NuguriException(ExceptionType.MONTH_TYPO)
    }
    val monthStart = DateTimeMaker.nowDate().withYear(year).withMonth(month).coreTimeStart.monthStartTime
    val monthEnd = DateTimeMaker.nowDate().withYear(year).withMonth(month).coreTimeStart.monthEndTime
    return userDayOffNuguriRepository.getTotalDayOffBetween(monthStart, monthEnd)
  }

  @Transactional
  fun thisMonthSickOffUsers(month: Int, year: Int): List<Tuple> {
    if (month > 12 || month < 1) {
      throw NuguriException(ExceptionType.MONTH_TYPO)
    }
    val monthStart = DateTimeMaker.nowDate().withYear(year).withMonth(month).coreTimeStart.monthStartTime
    val monthEnd = DateTimeMaker.nowDate().withYear(year).withMonth(month).coreTimeStart.monthEndTime
    return userDayOffNuguriRepository.getTotalSickDayOffBetween(monthStart, monthEnd)
  }

}