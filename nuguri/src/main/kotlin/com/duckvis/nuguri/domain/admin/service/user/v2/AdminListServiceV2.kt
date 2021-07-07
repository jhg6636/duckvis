package com.duckvis.nuguri.domain.admin.service.user.v2

import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.stereotype.Service

@Service("ADMIN_LIST_V2")
class AdminListServiceV2(
  private val userRepository: UserRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.ADMIN_LIST

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val admins = userRepository.findAll().filter { user ->
      user.isAdmin
    }
    return "관리자 목록입니다~\n" + admins.joinToString(", ") { admin -> admin.name }
  }

}