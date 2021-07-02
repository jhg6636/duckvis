package com.duckvis.nuguri.domain.admin.service.user

import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * 관리자 추가
 */
@Service("ADD_ADMIN")
class AddAdminService(
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
    addAdmin(serviceRequestDto.params[0])
    return "${serviceRequestDto.params[0]} 님이 새로운 관리자로 추가되었어요. 잘 부탁해요~"
  }

  @Transactional
  fun addAdmin(name: String) {
    val user = userRepository.findByName(name) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    if (user.isAdmin) {
      throw NuguriException(ExceptionType.ALREADY_ADMIN)
    }
    user.upgradeUserLevel()
  }

}