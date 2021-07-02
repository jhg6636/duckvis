package com.duckvis.nuguri.domain.admin.service.team

import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * 팀삭제 기능
 */
@Service("DELETE_TEAM")
class DeleteTeamService(
  @Autowired private val teamRepository: TeamRepository,
  @Autowired private val userTeamRepository: UserTeamRepository,
) : NuguriService {

  override val minimumRequestParams: Int
    get() = 1
  override val maximumRequestParams: Int
    get() = 1
  override val minimumPermission: ServicePermission
    get() = ServicePermission.ADMIN

  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    deleteTeam(serviceRequestDto.params[0])

    return "${serviceRequestDto.params[0]} 팀이 삭제되었어요"
  }

  @Transactional
  fun deleteTeam(name: String) {
    val team = teamRepository.findByName(name) ?: throw NuguriException(ExceptionType.NO_SUCH_TEAM)
    userTeamRepository.findAllByTeamId(team.id).forEach { userTeam -> userTeamRepository.delete(userTeam) }
    teamRepository.delete(team)
  }

}