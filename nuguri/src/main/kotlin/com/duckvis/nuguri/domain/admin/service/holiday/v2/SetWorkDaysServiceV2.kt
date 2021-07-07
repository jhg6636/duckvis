package com.duckvis.nuguri.domain.admin.service.holiday.v2

import com.duckvis.core.domain.nuguri.HolidayRepository
import com.duckvis.core.domain.nuguri.UserProfileRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.holiday.NuguriSetWorkdaysRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.core.utils.isNationalHoliday
import com.duckvis.core.utils.isWeekday
import com.duckvis.core.utils.isWeekend
import com.duckvis.nuguri.domain.admin.service.holiday.ThisMonthInfoService
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import javax.transaction.Transactional

@Service("SET_WORKDAYS_V2")
class SetWorkDaysServiceV2(
  @Autowired private val holidayRepository: HolidayRepository,
  @Autowired private val userProfileRepository: UserProfileRepository,
  @Autowired private val thisMonthInfoService: ThisMonthInfoService,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.SET_WORKDAYS

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val param = serviceRequestDto as NuguriSetWorkdaysRequestParameterDto
    val dates = param.dates
    setWorkDay(dates)
    return "${dates.joinToString(", ") { date -> "${date.dayOfMonth}일" }}이 근무일이 되었어요"
  }

  @Transactional
  fun setWorkDay(dates: List<LocalDate>) {
    dates.forEach { date ->
      if (!holidayRepository.existsByDateEquals(date) && date.isWeekday) {
        throw NuguriException(ExceptionType.NO_SUCH_HOLIDAY)
      }
      if (date.isWeekend) {
        throw NuguriException(ExceptionType.WEEKEND)
      }
      if (date.isNationalHoliday) {
        throw NuguriException(ExceptionType.NATIONAL_HOLIDAY)
      }
    }
    holidayRepository.deleteAll(dates.mapNotNull { holidayRepository.findByDateEquals(it) })
    userProfileRepository.findAll().forEach { profile ->
      profile.changeTargetWorkSeconds(
        thisMonthInfoService.thisMonthWorkingSeconds(
          dates.first().monthValue,
          DateTimeMaker.nowDate().year
        )
      )
    }
  }

}