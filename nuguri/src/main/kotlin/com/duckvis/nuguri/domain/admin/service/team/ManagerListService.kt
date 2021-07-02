package com.duckvis.nuguri.domain.admin.service.team

import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeam
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * !매니저목록 기능
 */
@Service("MANAGER_LIST")
class ManagerListService(
  @Autowired private val userTeamRepository: UserTeamRepository,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val teamRepository: TeamRepository,
) : NuguriService {

  override val minimumRequestParams = 0
  override val maximumRequestParams = 0
  override val minimumPermission = ServicePermission.MEMBER

  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    return "매니저 목록이에요~\n${getManagers().joinToString("\n") { manager -> managerString(manager) }}"
  }

  @Transactional
  fun getManagers(): List<UserTeam> {
    return userTeamRepository.findAll().filter { it.isManager }
  }

  private fun managerString(userTeam: UserTeam): String {
    val userName =
      userRepository.findByIdOrNull(userTeam.userId)?.name ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val teamName =
      teamRepository.findByIdOrNull(userTeam.teamId)?.name ?: throw NuguriException(ExceptionType.NO_SUCH_TEAM)
    return ":pushpin:$userName(${teamName}팀 매니저)"
  }

}