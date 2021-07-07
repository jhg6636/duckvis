package com.duckvis.nuguri.domain.admin.service.team.v2

import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeam
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.team.NuguriDeleteManagerRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.UserTeamLevel
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service("DELETE_MANAGER_V2")
class DeleteManagerServiceV2(
  private val userRepository: UserRepository,
  private val teamRepository: TeamRepository,
  private val userTeamRepository: UserTeamRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.DELETE_MANAGER

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriDeleteManagerRequestParameterDto
    deleteManager(params.teamName, params.managerName)
    return "${params.teamName}님이 ${params.managerName}팀 매니저에서 물러나요. 그동안 고생 많으셨어요~"
  }

  @Transactional
  fun deleteManager(teamName: String, managerName: String): UserTeam {
    val team = teamRepository.findByName(teamName) ?: throw NuguriException(ExceptionType.NO_SUCH_TEAM)
    val manager = userRepository.findByName(managerName) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    checkIfOnlyManager(manager.id, team.id)
    val userTeam = userTeamRepository.findByUserIdAndTeamId(manager.id, team.id)!!

    if (!userTeam.isManager) {
      throw NuguriException(ExceptionType.NOT_MANAGER)
    }

    userTeam.setLevel(UserTeamLevel.MEMBER)

    return userTeam
  }

  fun checkIfOnlyManager(userId: Long, teamId: Long) {
    val teamMembers = userTeamRepository.findAllByTeamId(teamId)
    val thisUser =
      userTeamRepository.findByUserIdAndTeamId(userId, teamId) ?: throw NuguriException(ExceptionType.NOT_TEAM_MEMBER)
    if (teamMembers.count { it.isManager } == 1 && thisUser.isManager) {
      throw NuguriException(ExceptionType.NOT_ENOUGH_MANAGER)
    }
  }
}