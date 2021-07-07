package com.duckvis.nuguri.domain.admin.service.team.v2

import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeam
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service("MANAGER_LIST_V2")
class ManagerListServiceV2(
  private val userTeamRepository: UserTeamRepository,
  private val userRepository: UserRepository,
  private val teamRepository: TeamRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.MANAGER_LIST

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
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