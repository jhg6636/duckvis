package com.duckvis.nuguri.domain.admin.service.project.v2

import com.duckvis.core.domain.nuguri.Project
import com.duckvis.core.domain.nuguri.ProjectRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.project.NuguriProjectEndRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service("PROJECT_END_V2")
class ProjectEndServiceV2(
  private val projectRepository: ProjectRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.PROJECT_END

  @Transactional
  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriProjectEndRequestParameterDto
    val project = endProject(params.projectName)

    return "${project.name} 프로젝트가 종료되었어요. 고생 많으셨어요~"
  }

  fun endProject(name: String): Project {
    val project = projectRepository.findByNameOrNickname(name, name)
      ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)
    project.deleteProject()
    return project
  }
}