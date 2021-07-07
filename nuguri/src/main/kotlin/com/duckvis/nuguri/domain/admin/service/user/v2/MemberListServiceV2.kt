package com.duckvis.nuguri.domain.admin.service.user.v2

import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service("MEMBER_LIST_V2")
class MemberListServiceV2(
  private val userRepository: UserRepository,
  private val userTeamRepository: UserTeamRepository,
  private val teamRepository: TeamRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.MEMBER_LIST

  @Transactional
  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val allUsers = userRepository.findAll()
      .filter { user -> !user.isGone && !user.isBot }
      .sortedBy { user -> teamNames(user) + user.name }

    return "셀렉트스타 멤버 목록이에요~\n${
      allUsers.joinToString("\n") { user ->
        ":pushpin:${user.name}${teamNames(user)}"
      }
    }"
  }

  private fun teamNames(user: User): String {
    val userTeams = userTeamRepository.findAllByUserId(user.id)
    if (userTeams.isEmpty()) {
      return " (팀 없음)"
    }
    return " (${
      userTeams.joinToString(", ") { userTeam ->
        "${teamRepository.findByIdOrNull(userTeam.teamId)?.name ?: throw NuguriException(ExceptionType.NO_SUCH_TEAM)} 팀"
      }
    })"
  }

}