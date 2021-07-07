package com.duckvis.nuguri.domain.admin.service.user.v2

import com.duckvis.core.domain.nuguri.UserProfile
import com.duckvis.core.domain.nuguri.UserProfileRepository
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.NuguriServiceRequestNoParameterDto
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.core.types.shared.UserPathType
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service("REGISTER_USER_V2")
class RegisterUserServiceV2(
  private val userRepository: UserRepository,
  private val userProfileRepository: UserProfileRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.REGISTER_USER

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriServiceRequestNoParameterDto
    return "${saveUser(params.userCode, params.userName).name} 님, 환영해요~"
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