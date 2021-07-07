package com.duckvis.nuguri.domain.admin.service.user.v2

import com.duckvis.core.domain.nuguri.UserDayOff
import com.duckvis.core.domain.nuguri.UserDayOffRepository
import com.duckvis.core.domain.nuguri.UserProfileRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.user.NuguriSetSickDayOffRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.DayOffType
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.core.utils.monthEndTime
import com.duckvis.core.utils.monthStartTime
import com.duckvis.nuguri.repository.UserDayOffNuguriRepository
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service("SET_SICK_DAY_OFF_V2")
class SetSickDayOffServiceV2(
  private val userRepository: UserRepository,
  private val userProfileRepository: UserProfileRepository,
  private val userDayOffRepository: UserDayOffRepository,
  private val userDayOffNuguriRepository: UserDayOffNuguriRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.SET_SICK_DAY_OFF

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriSetSickDayOffRequestParameterDto
    val totalDays = setDayOffSick(params.userName, params.days)
    return "이번 달 병가를 ${params.days}일 추가한다고 적어뒀어요~\n이제 이번 달에 사용하신 병가는 ${totalDays}일이에요~\n" +
      "아파요? 아프지 마요."
  }

  @Transactional
  fun setDayOffSick(userName: String, days: Int): Int {
    val user = userRepository.findByName(userName) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val profile = userProfileRepository.findByUserId(user.id) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    profile.changeDayOffSick(days)
    userDayOffRepository.save(UserDayOff(user.id, days, DayOffType.SICK))

    return userDayOffNuguriRepository.getMyTotalDayOffBetween(
      user.id,
      DayOffType.SICK,
      DateTimeMaker.nowDateTime().monthStartTime,
      DateTimeMaker.nowDateTime().monthEndTime
    )
  }

}