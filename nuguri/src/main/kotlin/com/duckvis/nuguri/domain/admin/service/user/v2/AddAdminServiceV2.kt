package com.duckvis.nuguri.domain.admin.service.user.v2

import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.user.NuguriAddAdminRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service("ADD_ADMIN_V2")
class AddAdminServiceV2(
  private val userRepository: UserRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.ADD_ADMIN

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriAddAdminRequestParameterDto
    addAdmin(params.name)
    return "${params.name} 님이 새로운 관리자로 추가되었어요. 잘 부탁해요~"
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