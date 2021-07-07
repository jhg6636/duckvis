package com.duckvis.nuguri.domain.admin.service.team.v2

import com.duckvis.core.domain.nuguri.Team
import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeam
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.team.NuguriAddManagerRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.UserTeamLevel
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service("ADD_MANAGER_V2")
class AddManagerServiceV2(
  private val userRepository: UserRepository,
  private val teamRepository: TeamRepository,
  private val userTeamRepository: UserTeamRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.ADD_MANAGER

  @Transactional
  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriAddManagerRequestParameterDto
    addManager(params.teamName, params.managerName)

    return "${params.managerName} 님이 ${params.teamName} 팀의 새로운 매니저가 되었어요. 잘 부탁해요~"
  }

  fun addManager(teamName: String, managerName: String): UserTeam {
    val team = teamRepository.findByName(teamName) ?: throw NuguriException(ExceptionType.NO_SUCH_TEAM)
    val manager = userRepository.findByName(managerName) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    return setManager(team, manager)
  }

  fun setManager(team: Team, manager: User): UserTeam {
    val userTeam = userTeamRepository.findByUserIdAndTeamId(manager.id, team.id)
      ?: throw NuguriException(ExceptionType.NOT_TEAM_MEMBER)
    if (userTeam.isManager) {
      throw NuguriException(ExceptionType.ALREADY_TEAM_MANAGER)
    }
    userTeam.setLevel(UserTeamLevel.MANAGER)

    return userTeam
  }

}