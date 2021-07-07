package com.duckvis.nuguri.domain.admin.service.user.v2

import com.duckvis.core.domain.nuguri.UserProfileRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.user.NuguriShowUserProfileRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service("SHOW_USER_PROFILE_V2")
class ShowUserProfileServiceV2(
  private val userProfileRepository: UserProfileRepository,
  private val userRepository: UserRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.SHOW_USER_PROFILE

  @Transactional
  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriShowUserProfileRequestParameterDto
    val user = userRepository.findByName(params.memberName) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val profile = userProfileRepository.findByUserId(user.id) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)

    return if (params.isDetail) {
      profile.detailProfileString
    } else {
      profile.profileString
    }
  }

}