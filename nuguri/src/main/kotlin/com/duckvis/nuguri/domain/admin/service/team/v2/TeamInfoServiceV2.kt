package com.duckvis.nuguri.domain.admin.service.team.v2

import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.team.NuguriTeamInfoRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.nuguri.domain.admin.dtos.TeamInformationDto
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service("TEAM_INFO_V2")
class TeamInfoServiceV2(
  private val teamRepository: TeamRepository,
  private val userRepository: UserRepository,
  private val userTeamRepository: UserTeamRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.TEAM_INFO

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriTeamInfoRequestParameterDto
    return teamInformation(params.name).toString()
  }

  @Transactional
  fun teamInformation(teamName: String): TeamInformationDto {
    val team = teamRepository.findByName(teamName) ?: throw NuguriException(ExceptionType.NO_SUCH_TEAM)
    val managers = userTeamRepository.findAllByTeamId(team.id).filter { userTeam -> userTeam.isManager }
      .mapNotNull { userTeam -> userRepository.findByIdOrNull(userTeam.userId) }
    val members = userTeamRepository.findAllByTeamId(team.id)
      .mapNotNull { userTeam -> userRepository.findByIdOrNull(userTeam.userId) }
    return TeamInformationDto(teamName, managers, members)
  }
}