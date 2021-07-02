package com.duckvis.nuguri.domain.admin.service.user

import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.stereotype.Service

@Service("ADMIN_LIST")
class AdminListService(
  private val userRepository: UserRepository,
) : NuguriService {

  override val minimumRequestParams: Int
    get() = 0
  override val maximumRequestParams: Int
    get() = 0
  override val minimumPermission: ServicePermission
    get() = ServicePermission.MEMBER

  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    val admins = userRepository.findAll().filter { user ->
      user.isAdmin
    }
    return "관리자 목록입니다~\n" + admins.joinToString(", ") { admin -> admin.name }
  }

}