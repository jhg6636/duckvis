package com.duckvis.nuguri.domain.statistics.service.v2

import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.statistics.NuguriThisMonthInfoRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.core.utils.*
import com.duckvis.nuguri.repository.HolidayNuguriRepository
import com.duckvis.nuguri.repository.UserDayOffNuguriRepository
import com.duckvis.nuguri.services.NuguriServiceV2
import com.querydsl.core.Tuple
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service("THIS_MONTH_INFO_V2")
class ThisMonthInfoServiceV2(
  @Autowired private val holidayNuguriRepository: HolidayNuguriRepository,
  @Autowired private val userDayOffNuguriRepository: UserDayOffNuguriRepository,
  @Autowired private val userRepository: UserRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.THIS_MONTH_INFO

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriThisMonthInfoRequestParameterDto
    params.checkMonthTypo()
    val offUsers = thisMonthOffUsers(params.month, params.year)
    val sickOffUsers = thisMonthSickOffUsers(params.month, params.year)
    return "${params.month}월의 달 정보에요~\n" +
      ":calendar:근무일: ${thisMonthWorkingDays(params.month, params.year)}일\n" +
      ":clock9:근무목표: ${thisMonthWorkingSeconds(params.month, params.year).secondsToString}\n\n" +
      "이 달의 휴가 사용 내역이에요~\n\n" +
      offUsers.joinToString("\n") { tuple ->
        val user = userRepository.findByIdOrNull(tuple.get(0, Long::class.java))
          ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
        "${user.name} ${tuple.get(1, Int::class.java)}일 사용"
      } +
      "\n\n이 달의 병가 사용 내역이에요~\n\n" +
      sickOffUsers.joinToString("\n") { tuple ->
        val user = userRepository.findByIdOrNull(tuple.get(0, Long::class.java))
          ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
        "${user.name} ${tuple.get(1, Int::class.java)}일 사용"
      }
  }

  @Transactional
  fun thisMonthWorkingDays(month: Int, year: Int): Int {
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
    val monthStart = DateTimeMaker.nowDate().withYear(year).withMonth(month).coreTimeStart.monthStartTime
    val monthEnd = DateTimeMaker.nowDate().withYear(year).withMonth(month).coreTimeStart.monthEndTime
    return userDayOffNuguriRepository.getTotalDayOffBetween(monthStart, monthEnd)
  }

  @Transactional
  fun thisMonthSickOffUsers(month: Int, year: Int): List<Tuple> {
    val monthStart = DateTimeMaker.nowDate().withYear(year).withMonth(month).coreTimeStart.monthStartTime
    val monthEnd = DateTimeMaker.nowDate().withYear(year).withMonth(month).coreTimeStart.monthEndTime
    return userDayOffNuguriRepository.getTotalSickDayOffBetween(monthStart, monthEnd)
  }

}