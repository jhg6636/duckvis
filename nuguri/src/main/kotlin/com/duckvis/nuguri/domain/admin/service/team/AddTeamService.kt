package com.duckvis.nuguri.domain.admin.service.team

import com.duckvis.core.domain.nuguri.Team
import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeam
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.UserTeamLevel
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * 팀 추가 기능
 */
@Service("ADD_TEAM")
class AddTeamService(
  @Autowired private val teamRepository: TeamRepository,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val userTeamRepository: UserTeamRepository,
) : NuguriService {

  override val minimumRequestParams: Int
    get() = 2
  override val maximumRequestParams: Int
    get() = 2
  override val minimumPermission: ServicePermission
    get() = ServicePermission.ADMIN

  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    addTeam(serviceRequestDto.params[0], serviceRequestDto.params[1])

    return "${serviceRequestDto.params[0]} 팀이 생성되었어요 (매니저: ${serviceRequestDto.params[1]})"
  }

  @Transactional
  fun addTeam(name: String, managerName: String): Team {
    checkIfAlreadyExistingTeamName(name)
    val managerId = userRepository.findByName(managerName)?.id ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val team = teamRepository.save(Team(name))
    userTeamRepository.save(UserTeam(managerId, team.id, UserTeamLevel.MANAGER))

    return team
  }

  @Transactional
  fun checkIfAlreadyExistingTeamName(name: String) {
    if (teamRepository.existsByName(name)) {
      throw NuguriException(ExceptionType.TEAM_NAME_ALREADY_EXISTS)
    }
  }

}