package com.duckvis.nuguri.domain.admin.service.holiday

import com.duckvis.core.domain.nuguri.HolidayRepository
import com.duckvis.core.domain.nuguri.UserProfileRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.utils.*
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

/**
 * !일하는날 기능 구현
 */
@Service("SET_WORKDAYS")
class SetWorkDaysService(
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
    setWorkDay(dates)
    return "${dates.joinToString(", ") { date -> "${date.dayOfMonth}일" }}이 근무일이 되었어요"
  }

  @Transactional
  fun setWorkDay(dates: List<LocalDate>) {
    dates.forEach {
      if (!holidayRepository.existsByDateEquals(it) && it.isWeekday) {
        throw NuguriException(ExceptionType.NO_SUCH_HOLIDAY)
      }
      if (it.isWeekend) {
        throw NuguriException(ExceptionType.WEEKEND)
      }
      if (it.isNationalHoliday) {
        throw NuguriException(ExceptionType.NATIONAL_HOLIDAY)
      }
    }
    holidayRepository.deleteAll(dates.mapNotNull { holidayRepository.findByDateEquals(it) })
    userProfileRepository.findAll().forEach { profile ->
      profile.changeTargetWorkSeconds(thisMonthInfoService.thisMonthWorkingSeconds(dates.first().monthValue, DateTimeMaker.nowDate().year))
    }
  }

}