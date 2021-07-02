package com.duckvis.nuguri.domain.admin.service.project

import com.duckvis.core.domain.nuguri.Project
import com.duckvis.core.domain.nuguri.ProjectRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 플젝종료 기능
 */
@Service("PROJECT_END")
class ProjectEndService(
  @Autowired private val projectRepository: ProjectRepository
) : NuguriService {

  override val minimumRequestParams = 1
  override val maximumRequestParams = 1
  override val minimumPermission = ServicePermission.MANAGER

  @Transactional
  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    val endedProject = endProject(serviceRequestDto.params[0])

    return "${endedProject.name} 프로젝트가 종료되었어요. 고생 많으셨어요~"
  }

  fun endProject(nameOrNickname: String): Project {
    val project =
      projectRepository.findByNameOrNickname(nameOrNickname, nameOrNickname)
        ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)
    project.deleteProject()
    return project
  }

}