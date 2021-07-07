package com.duckvis.nuguri.domain.admin.service.team.v2

import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.stereotype.Service

@Service("TEAM_LIST_V2")
class TeamListServiceV2(
  private val teamRepository: TeamRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.TEAM_LIST

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    return "팀 목록이에요~\n${teamRepository.findAll().joinToString("\n") { team -> ":pushpin:${team.name}팀" }}"
  }

}