package com.duckvis.nuguri.domain.attendance.service.v2

import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.NuguriServiceRequestNoParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.core.types.shared.UserPathType
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.core.utils.dayStartTime
import com.duckvis.nuguri.domain.attendance.dtos.HowLongIWorkedResponse
import com.duckvis.nuguri.repository.AttendanceCardNuguriRepository
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service("HOW_LONG_I_WORKED_V2")
class HowLongIWorkedServiceV2(
  private val attendanceCardNuguriRepository: AttendanceCardNuguriRepository,
  private val userRepository: UserRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.HOW_LONG_I_WORKED

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriServiceRequestNoParameterDto
    return howLongIWorked(params.userCode).toString()
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