package com.duckvis.nuguri.domain.attendance.service

import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.shared.UserPathType
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.core.utils.dayStartTime
import com.duckvis.nuguri.domain.attendance.dtos.HowLongIWorkedResponse
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.repository.AttendanceCardNuguriRepository
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * 몇시간 기능
 */
@Service("HOW_LONG_I_WORKED")
class HowLongIWorkedService(
  @Autowired private val attendanceCardNuguriRepository: AttendanceCardNuguriRepository,
  @Autowired private val userRepository: UserRepository,
) : NuguriService {

  override val minimumRequestParams: Int
    get() = 0
  override val maximumRequestParams: Int
    get() = 0
  override val minimumPermission: ServicePermission
    get() = ServicePermission.MEMBER

  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    return howLongIWorked(serviceRequestDto.userCode).toString()
  }

  @Transactional
  fun howLongIWorked(userCode: String): HowLongIWorkedResponse {
    val user = userRepository.findByCodeAndPath(userCode, UserPathType.SLACK)
      ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val nowWorkingCard =
      attendanceCardNuguriRepository.getMyWorkingCard(user.id) ?: throw NuguriException(ExceptionType.NOT_WORKING)
    val todayWorkingCards = attendanceCardNuguriRepository.getMyCardsBetween(
      user.id,
      DateTimeMaker.nowDateTime().dayStartTime,
      DateTimeMaker.nowDateTime()
    )

    val todayWorkingSeconds = todayWorkingCards
      .filter { card -> !card.isNowWorking }
      .sumBy { card -> card.durationSeconds ?: 0 }

    return HowLongIWorkedResponse(
      nowWorkingCard.nowWorkingSeconds,
      todayWorkingSeconds + nowWorkingCard.nowWorkingSeconds
    )
  }

}