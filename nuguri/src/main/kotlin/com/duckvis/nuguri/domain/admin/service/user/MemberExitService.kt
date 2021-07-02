package com.duckvis.nuguri.domain.admin.service.user

import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.User
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
 * !퇴사 기능
 */
@Service("MEMBER_EXIT")
class MemberExitService(
  @Autowired private val userRepository: UserRepository,
  @Autowired private val userTeamRepository: UserTeamRepository,
) : NuguriService {

  override val minimumRequestParams: Int
    get() = 1
  override val maximumRequestParams: Int
    get() = 1
  override val minimumPermission: ServicePermission
    get() = ServicePermission.ADMIN

  @Transactional
  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    exit(serviceRequestDto.params[0])

    return "${serviceRequestDto.params[0]} 님, 그 동안 고생하셨어요."
  }

  @Transactional
  fun leaveAllTeams(userId: Long) {
    val allUserTeams = userTeamRepository.findAllByUserId(userId)
    allUserTeams.forEach { userTeam ->
      println("${userTeam.userId}, ${userTeam.teamId}")
      checkIfOnlyManager(userTeam.userId, userTeam.teamId)
    }
    userTeamRepository.deleteAll(allUserTeams)
  }

  @Transactional
  fun checkIfOnlyManager(userId: Long, teamId: Long) {
    val teamMembers = userTeamRepository.findAllByTeamId(teamId)
    val thisUser =
      userTeamRepository.findByUserIdAndTeamId(userId, teamId) ?: throw NuguriException(ExceptionType.NOT_TEAM_MEMBER)
    val managerCount = teamMembers.count { userTeam -> userTeam.isManager }
    if (managerCount == 1 && thisUser.isManager) {
      throw NuguriException(ExceptionType.NOT_ENOUGH_MANAGER)
    }
  }

  @Transactional
  fun exit(userName: String): User {
    val user = userRepository.findByName(userName) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    leaveAllTeams(user.id)
    user.exit()

    return user
  }

}