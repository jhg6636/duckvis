package com.duckvis.nuguri.domain.admin.service.team.v2

import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.team.NuguriDeleteTeamMemberRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service("DELETE_TEAM_MEMBER_V2")
class DeleteTeamMemberServiceV2(
  private val teamRepository: TeamRepository,
  private val userRepository: UserRepository,
  private val userTeamRepository: UserTeamRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.DELETE_TEAM_MEMBER

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriDeleteTeamMemberRequestParameterDto
    deleteTeamMember(params.memberName, params.teamName)

    return "${params.memberName} 님이 ${params.teamName} 팀을 떠나게 되었어요. 그동안 고생하셨어요~"
  }

  @Transactional
  fun deleteTeamMember(userName: String, teamName: String) {
    val userId = userRepository.findByName(userName)?.id ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val teamId = teamRepository.findByName(teamName)?.id ?: throw NuguriException(ExceptionType.NO_SUCH_TEAM)
    checkIfOnlyManager(userId, teamId)
    val userTeam =
      userTeamRepository.findByUserIdAndTeamId(userId, teamId) ?: throw NuguriException(ExceptionType.NOT_TEAM_MEMBER)

    userTeamRepository.delete(userTeam)
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