package com.duckvis.nuguri.domain.admin.service.user

import com.duckvis.core.domain.nuguri.UserProfileRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 사원 정보 보기 기능
 */
@Service("SHOW_USER_PROFILE")
class ShowUserProfileService(
  @Autowired private val userProfileRepository: UserProfileRepository,
  @Autowired private val userRepository: UserRepository,
) : NuguriService {

  override val minimumRequestParams: Int
    get() = 0
  override val maximumRequestParams: Int
    get() = 1
  override val minimumPermission: ServicePermission
    get() = ServicePermission.MEMBER

  @Transactional
  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    val user = userRepository.findByName(serviceRequestDto.params.singleOrNull() ?: serviceRequestDto.userName)
      ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val isDetail = serviceRequestDto.text.contains("세부")
    val profile = userProfileRepository.findByUserId(user.id) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    return if (isDetail) {
      profile.detailProfileString
    } else {
      profile.profileString
    }
  }

}