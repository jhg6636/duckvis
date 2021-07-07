package com.duckvis.nuguri.domain.admin.service.project.v2

import com.duckvis.core.domain.nuguri.ProjectRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.nuguri.dtos.ServiceRequestDtoV2
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.stereotype.Service

@Service("PROJECT_LIST_V2")
class ProjectListServiceV2(
  private val projectRepository: ProjectRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.PROJECT_LIST

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val activeProjects = projectRepository.findAll()
      .filter { project ->
        !project.isFinished
      }
    return "프로젝트 목록이에요~\n${activeProjects.joinToString("\n") { project -> ":pushpin:${project.fullName}" }}"
  }

}