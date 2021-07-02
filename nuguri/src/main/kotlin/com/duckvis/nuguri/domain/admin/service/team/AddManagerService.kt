package com.duckvis.nuguri.domain.admin.service.team

import com.duckvis.core.domain.nuguri.Team
import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeam
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.User
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
 * 팀장 추가 기능
 */
@Service("ADD_MANAGER")
class AddManagerService(
  @Autowired private val userRepository: UserRepository,
  @Autowired private val teamRepository: TeamRepository,
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
    addManager(serviceRequestDto.params[0], serviceRequestDto.params[1])

    return "${serviceRequestDto.params[1]} 님이 ${serviceRequestDto.params[0]} 팀의 새로운 매니저가 되었어요. 잘 부탁해요~"
  }

  @Transactional
  fun addManager(teamName: String, managerName: String): UserTeam {
    val team = teamRepository.findByName(teamName) ?: throw NuguriException(ExceptionType.NO_SUCH_TEAM)
    val manager = userRepository.findByName(managerName) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    return setManager(team, manager)
  }

  @Transactional
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