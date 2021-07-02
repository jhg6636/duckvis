package com.duckvis.nuguri.domain.admin.service.team

import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeam
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
 * 팀원추가 기능
 */
@Service("ADD_TEAM_MEMBER")
class AddTeamMemberService(
  @Autowired private val userRepository: UserRepository,
  @Autowired private val teamRepository: TeamRepository,
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
    addTeamMember(serviceRequestDto.params[1], serviceRequestDto.params[0])
    return "${serviceRequestDto.params[1]} 님이 ${serviceRequestDto.params[0]} 팀에 합류하셨어요\n환영해요~"
  }

  @Transactional
  fun addTeamMember(userName: String, teamName: String): UserTeam {
    val userId = userRepository.findByName(userName)?.id ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val teamId = teamRepository.findByName(teamName)?.id ?: throw NuguriException(ExceptionType.NO_SUCH_TEAM)
    if (userTeamRepository.findByUserIdAndTeamId(userId, teamId) != null) {
      throw NuguriException(ExceptionType.TEAM_MEMBER_ALREADY_EXISTS)
    }
    return userTeamRepository.save(UserTeam(userId, teamId))
  }

}