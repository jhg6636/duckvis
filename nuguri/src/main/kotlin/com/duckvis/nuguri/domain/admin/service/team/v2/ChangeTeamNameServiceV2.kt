package com.duckvis.nuguri.domain.admin.service.team.v2

import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.team.NuguriChangeTeamNameRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service("CHANGE_TEAM_NAME_V2")
class ChangeTeamNameServiceV2(
  private val teamRepository: TeamRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.CHANGE_TEAM_NAME

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriChangeTeamNameRequestParameterDto
    changeTeamName(params.originalName, params.newName)
    return "${params.originalName} 팀이 ${params.newName} 팀으로 이름이 변경되었어요~ 바꾼 이름 많이 불러주세요~"
  }

  @Transactional
  fun changeTeamName(originalName: String, newName: String) {
    if (originalName == newName) throw NuguriException(ExceptionType.CANNOT_CHANGE)
    val originalTeam = teamRepository.findByName(originalName) ?: throw NuguriException(ExceptionType.NO_SUCH_TEAM)
    val teamNames = teamRepository.findAll().map { team -> team.name }
    if (teamNames.any { teamName -> teamName == newName }) {
      throw NuguriException(ExceptionType.TEAM_NAME_ALREADY_EXISTS)
    }
    originalTeam.changeName(newName)
  }

}