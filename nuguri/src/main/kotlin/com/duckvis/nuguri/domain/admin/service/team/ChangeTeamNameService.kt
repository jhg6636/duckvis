package com.duckvis.nuguri.domain.admin.service.team

import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 팀명변경 기능
 */
@Service("CHANGE_TEAM_NAME")
class ChangeTeamNameService(
  @Autowired private val teamRepository: TeamRepository,
) : NuguriService {

  override val minimumRequestParams: Int
    get() = 2

  override val maximumRequestParams: Int
    get() = 2

  override val minimumPermission: ServicePermission
    get() = ServicePermission.ADMIN

  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    changeTeamName(serviceRequestDto.params[0], serviceRequestDto.params[1])
    return "${serviceRequestDto.params[0]} 팀이 ${serviceRequestDto.params[1]} 팀으로 이름이 변경되었어요~ 바꾼 이름 많이 불러주세요~"
  }

  @Transactional
  fun changeTeamName(originalName: String, newName: String) {
    if (originalName == newName) throw NuguriException(ExceptionType.CANNOT_CHANGE)
    val team = teamRepository.findByName(originalName) ?: throw NuguriException(ExceptionType.NO_SUCH_TEAM)
    val teamNames = teamRepository.findAll().map { team -> team.name }
    if (teamNames.any { teamName -> teamName == newName }) {
      throw NuguriException(ExceptionType.TEAM_NAME_ALREADY_EXISTS)
    }
    team.changeName(newName)
  }

}