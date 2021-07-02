package com.duckvis.nuguri.domain.admin.service.team

import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.nuguri.domain.admin.dtos.TeamInformationDto
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * 팀 정보 기능
 */
@Service("TEAM_INFO")
class TeamInfoService(
  @Autowired private val teamRepository: TeamRepository,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val userTeamRepository: UserTeamRepository,
) : NuguriService {

  override val minimumRequestParams = 1
  override val maximumRequestParams = 1
  override val minimumPermission = ServicePermission.MEMBER

  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    return teamInformation(serviceRequestDto.params[0]).toString()
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