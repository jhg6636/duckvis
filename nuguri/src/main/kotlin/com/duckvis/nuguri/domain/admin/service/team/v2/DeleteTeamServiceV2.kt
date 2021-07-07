package com.duckvis.nuguri.domain.admin.service.team.v2

import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.team.NuguriDeleteTeamRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service("DELETE_TEAM_V2")
class DeleteTeamServiceV2(
  private val teamRepository: TeamRepository,
  private val userTeamRepository: UserTeamRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.DELETE_TEAM

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriDeleteTeamRequestParameterDto
    deleteTeam(params.teamName)

    return "${params.teamName} 팀이 삭제되었어요"
  }

  @Transactional
  fun deleteTeam(name: String) {
    val team = teamRepository.findByName(name) ?: throw NuguriException(ExceptionType.NO_SUCH_TEAM)
    userTeamRepository.findAllByTeamId(team.id).forEach { userTeam -> userTeamRepository.delete(userTeam) }
    teamRepository.delete(team)
  }

}