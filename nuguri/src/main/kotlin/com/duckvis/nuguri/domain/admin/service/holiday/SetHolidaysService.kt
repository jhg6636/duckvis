package com.duckvis.nuguri.domain.admin.service.holiday

import com.duckvis.core.domain.nuguri.Holiday
import com.duckvis.core.domain.nuguri.HolidayRepository
import com.duckvis.core.domain.nuguri.UserProfileRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.HolidayType
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.core.utils.dayToDates
import com.duckvis.core.utils.isNationalHoliday
import com.duckvis.core.utils.isWeekend
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

/**
 * !쉬는날 기능
 */
@Service("SET_HOLIDAYS")
class SetHolidaysService(
  @Autowired private val holidayRepository: HolidayRepository,
  @Autowired private val userProfileRepository: UserProfileRepository,
  @Autowired private val thisMonthInfoService: ThisMonthInfoService,
) : NuguriService {

  override val minimumRequestParams: Int
    get() = 1
  override val maximumRequestParams: Int
    get() = 1
  override val minimumPermission: ServicePermission
    get() = ServicePermission.ADMIN

  override fun checkTypo(serviceRequestDto: ServiceRequestDto) {
    super.checkTypo(serviceRequestDto)
    val monthLength = DateTimeMaker.nowDate().lengthOfMonth()
    serviceRequestDto.params[0].split(",").forEach { day ->
      if (day.toInt() > monthLength) {
        throw NuguriException(ExceptionType.OVER_MONTH_LENGTH)
      }
    }
  }

  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    val dates = serviceRequestDto.params[0].dayToDates
    val holidays = setHolidays(dates, HolidayType.SPECIAL)
    return "${holidays.joinToString(", ") { holiday -> "${holiday.dayString}일" }}이 휴일로 지정되었어요~"
  }

  @Transactional
  fun setHolidays(days: List<LocalDate>, type: HolidayType): List<Holiday> {
    val setHolidays = days.map { day ->
      val holiday = Holiday(day, type)
      if (holidayRepository.existsByDateEquals(day)) {
        throw NuguriException(ExceptionType.ALREADY_HOLIDAY)
      }
      if (day.isWeekend) {
        throw NuguriException(ExceptionType.WEEKEND)
      }
      if (day.isNationalHoliday) {
        throw NuguriException(ExceptionType.NATIONAL_HOLIDAY)
      }
      holidayRepository.save(holiday)
    }
    userProfileRepository.findAll().forEach { profile ->
      profile.changeTargetWorkSeconds(thisMonthInfoService.thisMonthWorkingSeconds(days.first().monthValue, DateTimeMaker.nowDate().year))
    }
    return setHolidays
  }

}