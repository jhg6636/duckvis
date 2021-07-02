package com.duckvis.nuguri.domain.admin.service.user

import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.shared.UserLevelType
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * 관리자 삭제 기능
 */
@Service("DELETE_ADMIN")
class DeleteAdminService(
  @Autowired private val userRepository: UserRepository,
) : NuguriService {

  override val minimumRequestParams: Int
    get() = 1
  override val maximumRequestParams: Int
    get() = 1
  override val minimumPermission: ServicePermission
    get() = ServicePermission.ADMIN

  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    deleteAdmin(serviceRequestDto.params[0])

    return "${serviceRequestDto.params[0]} 님이 관리자에서 물러났어요. 그 동안 고생하셨어요~"
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