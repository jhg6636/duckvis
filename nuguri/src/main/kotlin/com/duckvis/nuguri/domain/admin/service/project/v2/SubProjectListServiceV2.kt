package com.duckvis.nuguri.domain.admin.service.project.v2

import com.duckvis.core.domain.nuguri.ProjectRepository
import com.duckvis.core.domain.nuguri.SubProjectRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.project.NuguriSubProjectListRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.stereotype.Service

@Service("SUB_PROJECT_LIST_V2")
class SubProjectListServiceV2(
  private val projectRepository: ProjectRepository,
  private val subProjectRepository: SubProjectRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.SUB_PROJECT_LIST

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriSubProjectListRequestParameterDto
    val project = projectRepository.findByNameOrNickname(params.projectNameOrNickname, params.projectNameOrNickname)
      ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)
    val activeSubProjects = subProjectRepository.findAllByProjectId(project.id)
      .filter { subProject ->
        !subProject.isFinished
      }
    return "${project.name} 프로젝트의 모든 서브플젝 목록이에요~\n\n" +
      activeSubProjects.joinToString("\n") { subProject ->
        ":pushpin:${subProject.name}(${subProject.nickname})"
      }
  }
}