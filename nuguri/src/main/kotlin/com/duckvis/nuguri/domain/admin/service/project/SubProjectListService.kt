package com.duckvis.nuguri.domain.admin.service.project

import com.duckvis.core.domain.nuguri.ProjectRepository
import com.duckvis.core.domain.nuguri.SubProjectRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.stereotype.Service

/**
 * !서브플젝목록
 */
@Service("SUB_PROJECT_LIST")
class SubProjectListService(
  private val projectRepository: ProjectRepository,
  private val subProjectRepository: SubProjectRepository,
) : NuguriService {

  override val minimumRequestParams: Int
    get() = 1
  override val maximumRequestParams: Int
    get() = 1
  override val minimumPermission: ServicePermission
    get() = ServicePermission.MEMBER

  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    val project = projectRepository.findByNameOrNickname(serviceRequestDto.params[0], serviceRequestDto.params[0])
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