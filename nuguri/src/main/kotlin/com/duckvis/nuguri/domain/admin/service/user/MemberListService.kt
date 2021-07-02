package com.duckvis.nuguri.domain.admin.service.user

import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service("MEMBER_LIST")
class MemberListService(
  @Autowired private val userRepository: UserRepository,
  @Autowired private val userTeamRepository: UserTeamRepository,
  @Autowired private val teamRepository: TeamRepository,
): NuguriService {

  override val maximumRequestParams: Int
    get() = 0
  override val minimumRequestParams: Int
    get() = 0
  override val minimumPermission: ServicePermission
    get() = ServicePermission.MEMBER

  override fun response(serviceRequestDto: ServiceRequestDto): String {
    val allUsers = userRepository.findAll()
      .filter { user -> !user.isGone && !user.isBot }
      .sortedBy { user -> teamNames(user) + user.name }

    return "셀렉트스타 멤버 목록이에요~\n${allUsers.joinToString("\n") { user ->
      ":pushpin:${user.name}${teamNames(user)}"
    }}"
  }

  private fun teamNames(user: User): String {
    val userTeams = userTeamRepository.findAllByUserId(user.id)
    if (userTeams.isEmpty()) {
      return " (팀 없음)"
    }
    return " (${userTeams.joinToString(", ") { userTeam -> 
      "${teamRepository.findByIdOrNull(userTeam.teamId)?.name ?: throw NuguriException(ExceptionType.NO_SUCH_TEAM)} 팀"
    }})"
  }
}