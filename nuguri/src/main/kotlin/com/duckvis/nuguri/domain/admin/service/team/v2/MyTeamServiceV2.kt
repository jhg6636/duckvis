package com.duckvis.nuguri.domain.admin.service.team.v2

import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.core.types.shared.UserPathType
import com.duckvis.nuguri.domain.admin.dtos.TeamInformationDto
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service("MY_TEAM_V2")
class MyTeamServiceV2(
  private val userTeamRepository: UserTeamRepository,
  private val userRepository: UserRepository,
  private val teamRepository: TeamRepository,
  private val teamInfoServiceV2: TeamInfoServiceV2,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.MY_TEAM

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    return myTeam(serviceRequestDto.userCode).joinToString("\n\n")
  }

  @Transactional
  fun myTeam(userCode: String): List<TeamInformationDto> {
    val userId = userRepository.findByCodeAndPath(userCode, UserPathType.SLACK)?.id ?: throw NuguriException(
      ExceptionType.NO_SUCH_USER
    )
    val teamNames =
      userTeamRepository.findAllByUserId(userId)
        .mapNotNull { userTeam -> teamRepository.findByIdOrNull(userTeam.teamId) }
        .map { it.name }
    return teamNames.map { teamName -> teamInfoServiceV2.teamInformation(teamName) }
  }

}