package com.duckvis.nuguri.domain.admin.service.project

import com.duckvis.core.domain.nuguri.ProjectRepository
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * !플젝목록 기능
 */
@Service("PROJECT_LIST")
class ProjectListService(
  @Autowired private val projectRepository: ProjectRepository,
) : NuguriService {

  override val minimumRequestParams = 0
  override val maximumRequestParams = 0
  override val minimumPermission = ServicePermission.MEMBER

  @Transactional
  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    val activeProjects = projectRepository.findAll()
      .filter { project ->
        !project.isFinished
      }
    return "프로젝트 목록이에요~\n${activeProjects.joinToString("\n") { project -> ":pushpin:${project.fullName}" }}"
  }

}