package com.duckvis.nuguri.domain.admin.service.project.v2

import com.duckvis.core.domain.nuguri.ProjectRepository
import com.duckvis.core.domain.nuguri.SubProjectRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.project.NuguriDeleteSubProjectRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.stereotype.Service

@Service("DELETE_SUB_PROJECT_V2")
class DeleteSubProjectServiceV2(
  private val projectRepository: ProjectRepository,
  private val subProjectRepository: SubProjectRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.DELETE_SUB_PROJECT

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriDeleteSubProjectRequestParameterDto

    val project = projectRepository.findByNameOrNickname(params.projectName, params.projectName)
      ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)

    val subProject = subProjectRepository.findByNameAndProjectId(params.subProjectName, project.id)
      ?: throw NuguriException(ExceptionType.NO_SUCH_SUB_PROJECT)

    subProject.deleteSubProject()
    return "${project.name} 플젝의 ${subProject.name} 서브플젝이 제거되었어요~"
  }

}