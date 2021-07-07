package com.duckvis.nuguri.domain.admin.service.user.v2

import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.user.NuguriMemberExitRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service("MEMBER_EXIT_V2")
class MemberExitServiceV2(
  private val userRepository: UserRepository,
  private val userTeamRepository: UserTeamRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.MEMBER_EXIT

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriMemberExitRequestParameterDto
    exit(params.memberName)

    return "${params.memberName} 님, 그 동안 고생하셨어요."
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