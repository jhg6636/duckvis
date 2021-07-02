package com.duckvis.nuguri.domain.admin.service.team

import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * 팀원삭제 기능
 */
@Service("DELETE_TEAM_MEMBER")
class DeleteTeamMemberService(
  @Autowired private val teamRepository: TeamRepository,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val userTeamRepository: UserTeamRepository,
) : NuguriService {

  override val minimumRequestParams: Int
    get() = 2
  override val maximumRequestParams: Int
    get() = 2
  override val minimumPermission: ServicePermission
    get() = ServicePermission.MANAGER

  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    deleteTeamMember(serviceRequestDto.params[1], serviceRequestDto.params[0])

    return "${serviceRequestDto.params[1]} 님이 ${serviceRequestDto.params[0]} 팀을 떠나게 되었어요. 그동안 고생하셨어요~"
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