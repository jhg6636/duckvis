package com.duckvis.nuguri.domain.admin.service.project.v2

import com.duckvis.core.domain.nuguri.ProjectRepository
import com.duckvis.core.domain.nuguri.SubProject
import com.duckvis.core.domain.nuguri.SubProjectRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.project.NuguriAddSubProjectRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.nuguri.repository.SubProjectNuguriRepository
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.stereotype.Service

@Service("ADD_SUB_PROJECT_V2")
class AddSubProjectServiceV2(
  private val subProjectRepository: SubProjectRepository,
  private val projectRepository: ProjectRepository,
  private val subProjectNuguriRepository: SubProjectNuguriRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.ADD_SUB_PROJECT

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriAddSubProjectRequestParameterDto
    val project = projectRepository.findByNameOrNickname(params.projectName, params.projectName)
      ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)

    if (subProjectNuguriRepository.isAlreadyExisting(params.name, params.nickname, project.id)) {
      throw NuguriException(ExceptionType.SUB_PROJECT_NAME_OR_NICKNAME_ALREADY_EXISTS)
    }

    val subProject = subProjectRepository.save(SubProject(project.id, params.name, params.nickname))

    return "${subProject.name} 서브플젝이 ${project.name} 프로젝트 아래에 생성되었어요~"
  }

}