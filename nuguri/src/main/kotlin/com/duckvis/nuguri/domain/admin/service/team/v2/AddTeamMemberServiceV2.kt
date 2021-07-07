package com.duckvis.nuguri.domain.admin.service.team.v2

import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeam
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.team.NuguriAddTeamMemberRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service("ADD_TEAM_MEMBER_V2")
class AddTeamMemberServiceV2(
  private val userRepository: UserRepository,
  private val teamRepository: TeamRepository,
  private val userTeamRepository: UserTeamRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.ADD_TEAM_MEMBER

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriAddTeamMemberRequestParameterDto
    addTeamMember(params.memberName, params.teamName)
    return "${params.memberName} 님이 ${params.teamName} 팀에 합류하셨어요\n환영해요~"
  }

  @Transactional
  fun addTeamMember(userName: String, teamName: String): UserTeam {
    val userId = userRepository.findByName(userName)?.id ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val teamId = teamRepository.findByName(teamName)?.id ?: throw NuguriException(ExceptionType.NO_SUCH_TEAM)
    if (userTeamRepository.findByUserIdAndTeamId(userId, teamId) != null) {
      throw NuguriException(ExceptionType.TEAM_MEMBER_ALREADY_EXISTS)
    }
    return userTeamRepository.save(UserTeam(userId, teamId))
  }

}