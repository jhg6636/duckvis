package com.duckvis.nuguri.domain.admin.service.user.v2

import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.user.NuguriDeleteAdminRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.core.types.shared.UserLevelType
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service("DELETE_ADMIN_V2")
class DeleteAdminServiceV2(
  private val userRepository: UserRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.DELETE_ADMIN

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriDeleteAdminRequestParameterDto
    deleteAdmin(params.name)

    return "${params.name} 님이 관리자에서 물러났어요. 그 동안 고생하셨어요~"
  }

  @Transactional
  fun deleteAdmin(name: String): User {
    val user = userRepository.findByName(name) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    if (user.level == UserLevelType.NORMAL) {
      throw NuguriException(ExceptionType.NOT_ADMIN)
    }
    user.downgradeUserLevel()

    return user
  }

}