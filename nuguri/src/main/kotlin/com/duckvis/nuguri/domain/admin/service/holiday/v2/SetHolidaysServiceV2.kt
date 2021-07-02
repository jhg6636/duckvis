package com.duckvis.nuguri.domain.admin.service.holiday.v2

import com.duckvis.core.domain.nuguri.Holiday
import com.duckvis.core.domain.nuguri.HolidayRepository
import com.duckvis.core.domain.nuguri.UserProfileRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.HolidayType
import com.duckvis.core.types.nuguri.service.CommandMajorType
import com.duckvis.core.types.nuguri.service.params.NuguriAdminRequestParameterDto
import com.duckvis.core.types.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.nuguri.dtos.ServiceRequestDtoV2
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class SetHolidaysServiceV2(
  @Autowired private val holidayRepository: HolidayRepository,
  @Autowired private val userProfileRepository: UserProfileRepository,
) : NuguriServiceV2 {

  override val majorType: CommandMajorType
    get() = CommandMajorType.ADMIN

  override fun response(serviceRequestDto: ServiceRequestDtoV2): String {
    val params = serviceRequestDto.parameter
    parameterCheck(params)
    params as NuguriAdminRequestParameterDto

    val dates = params.dates
    checkAlreadyHoliday(dates)

    val holidays = dates.map { date ->
      holidayRepository.save(Holiday(date, HolidayType.SPECIAL))
    }
    updateAllProfiles(holidays.size)

    return "${
      holidays.joinToString(", ") { holiday ->
        "${holiday.dayString}일"
      }
    }이 휴일로 지정되었어요~"
  }

  override fun parameterCheck(requestParameterDto: NuguriServiceRequestParameterDto) {
    if (requestParameterDto !is NuguriAdminRequestParameterDto) {
      throw NuguriException(ExceptionType.TYPO)
    }
  }

  private fun checkAlreadyHoliday(dates: List<LocalDate>) {
    dates.forEach { date ->
      if (holidayRepository.existsByDateEquals(date)) {
        throw NuguriException(ExceptionType.ALREADY_HOLIDAY)
      }
    }
  }

  private fun updateAllProfiles(days: Int) {
    userProfileRepository.findAll()
      .forEach { userProfile ->
        userProfile.minusTargetSeconds(days * 8 * 60 * 60)
      }
  }

}