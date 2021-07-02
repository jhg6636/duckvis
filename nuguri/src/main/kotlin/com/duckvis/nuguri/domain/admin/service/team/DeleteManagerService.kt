package com.duckvis.nuguri.domain.admin.service.team

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
import org.springframework.transaction.annotation.Transactional

@Service("DELETE_MANAGER")
class DeleteManagerService(
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
    deleteManager(serviceRequestDto.params[0], serviceRequestDto.params[1])
    return "${serviceRequestDto.params[1]}님이 ${serviceRequestDto.params[0]}팀 매니저에서 물러나요. 그동안 고생 많으셨어요~"
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

  @Transactional
  fun checkIfOnlyManager(userId: Long, teamId: Long) {
    val teamMembers = userTeamRepository.findAllByTeamId(teamId)
    val thisUser =
      userTeamRepository.findByUserIdAndTeamId(userId, teamId) ?: throw NuguriException(ExceptionType.NOT_TEAM_MEMBER)
    if (teamMembers.count { it.isManager } == 1 && thisUser.isManager) {
      throw NuguriException(ExceptionType.NOT_ENOUGH_MANAGER)
    }
  }

}