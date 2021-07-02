package com.duckvis.nuguri.domain.admin.service.user

import com.duckvis.core.domain.nuguri.UserProfile
import com.duckvis.core.domain.nuguri.UserProfileRepository
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.types.shared.UserPathType
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * !입사 기능
 */
@Service("REGISTER_USER")
class RegisterUserService(
  @Autowired private val userRepository: UserRepository,
  @Autowired private val userProfileRepository: UserProfileRepository,
) : NuguriService {

  override val minimumRequestParams = 0
  override val maximumRequestParams = 0
  override val minimumPermission = ServicePermission.EVERYBODY

  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    return "${saveUser(serviceRequestDto.userCode, serviceRequestDto.userName).name} 님, 환영해요~"
  }

  @Transactional
  fun saveUser(code: String, name: String): User {
    val user = userRepository.findByCodeAndPath(code, UserPathType.SLACK) ?: userRepository.save(User(code, name))
    if (user.isGone) {
      user.rejoin()
    }
    userProfileRepository.findByUserId(user.id)
      ?: userProfileRepository.save(UserProfile(user.id, user.name))
    return user
  }

}