package com.duckvis.nuguri.domain.admin.service.user

import com.duckvis.core.domain.nuguri.UserDayOff
import com.duckvis.core.domain.nuguri.UserDayOffRepository
import com.duckvis.core.domain.nuguri.UserProfileRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.DayOffType
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.core.utils.monthEndTime
import com.duckvis.core.utils.monthStartTime
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.repository.UserDayOffNuguriRepository
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * !휴가 3 기능 구현
 */
@Service("SET_DAY_OFF")
class SetDayOffService(
  @Autowired private val userRepository: UserRepository,
  @Autowired private val userProfileRepository: UserProfileRepository,
  @Autowired private val userDayOffRepository: UserDayOffRepository,
  @Autowired private val userDayOffNuguriRepository: UserDayOffNuguriRepository,
) : NuguriService {

  override val minimumRequestParams: Int
    get() = 1
  override val maximumRequestParams: Int
    get() = 1
  override val minimumPermission: ServicePermission
    get() = ServicePermission.MEMBER

  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    val myTotalDays = setDayOff(serviceRequestDto.userName, serviceRequestDto.params[0].toInt())

    return "이번 달 휴가를 ${serviceRequestDto.params[0]}일 추가한다고 적어 뒀어요~\n이제 이번 달에 사용하신 휴가는 ${myTotalDays}일이에요~\n" +
      "푹 쉬세요~\n"
  }

  /**
   * @return 이번 달 휴가 총합
   */
  @Transactional
  fun setDayOff(userName: String, days: Int): Int {
    val user = userRepository.findByName(userName) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val profile = userProfileRepository.findByUserId(user.id) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    profile.changeDayOff(days)
    userDayOffRepository.save(UserDayOff(user.id, days, DayOffType.NORMAL))

    return userDayOffNuguriRepository.getMyTotalDayOffBetween(
      user.id,
      DayOffType.NORMAL,
      DateTimeMaker.nowDateTime().monthStartTime,
      DateTimeMaker.nowDateTime().monthEndTime
    )
  }

}