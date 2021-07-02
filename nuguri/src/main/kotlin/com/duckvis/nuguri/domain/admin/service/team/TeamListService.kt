package com.duckvis.nuguri.domain.admin.service.team

import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * !팀목록 기능
 */
@Service("TEAM_LIST")
class TeamListService(
  @Autowired private val teamRepository: TeamRepository,
) : NuguriService {

  override val minimumRequestParams = 0
  override val maximumRequestParams = 0
  override val minimumPermission = ServicePermission.MEMBER

  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    return "팀 목록이에요~\n${teamRepository.findAll().joinToString("\n") { team -> ":pushpin:${team.name}팀" }}"
  }

}