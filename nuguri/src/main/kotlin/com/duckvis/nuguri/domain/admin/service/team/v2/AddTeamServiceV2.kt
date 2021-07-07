package com.duckvis.nuguri.domain.admin.service.team.v2

import com.duckvis.core.domain.nuguri.Team
import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeam
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.team.NuguriAddTeamRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.UserTeamLevel
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.nuguri.dtos.ServiceRequestDtoV2
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service("ADD_TEAM_V2")
class AddTeamServiceV2(
  private val teamRepository: TeamRepository,
  private val userRepository: UserRepository,
  private val userTeamRepository: UserTeamRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.ADD_TEAM

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriAddTeamRequestParameterDto
    addTeam(params.teamName, params.managerName)

    return "${params.teamName} 팀이 생성되었어요 (매니저: ${params.managerName})"
  }

  @Transactional
  fun addTeam(name: String, managerName: String): Team {
    checkIfAlreadyExistingTeamName(name)
    val managerId = userRepository.findByName(managerName)?.id ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val team = teamRepository.save(Team(name))
    userTeamRepository.save(UserTeam(managerId, team.id, UserTeamLevel.MANAGER))

    return team
  }

  fun checkIfAlreadyExistingTeamName(name: String) {
    if (teamRepository.existsByName(name)) {
      throw NuguriException(ExceptionType.TEAM_NAME_ALREADY_EXISTS)
    }
  }

}