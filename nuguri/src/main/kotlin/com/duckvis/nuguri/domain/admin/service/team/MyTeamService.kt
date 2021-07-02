package com.duckvis.nuguri.domain.admin.service.team

import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.shared.UserPathType
import com.duckvis.nuguri.domain.admin.dtos.TeamInformationDto
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * !내팀 기능
 */
@Service("MY_TEAM")
class MyTeamService(
  @Autowired private val userTeamRepository: UserTeamRepository,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val teamRepository: TeamRepository,
  @Autowired private val teamInfoService: TeamInfoService,
) : NuguriService {

  override val minimumRequestParams = 0
  override val maximumRequestParams = 0
  override val minimumPermission = ServicePermission.MEMBER

  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
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
    return teamNames.map { teamName -> teamInfoService.teamInformation(teamName) }
  }

}