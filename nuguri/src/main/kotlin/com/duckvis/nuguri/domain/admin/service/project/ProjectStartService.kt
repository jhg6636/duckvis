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
 * !플젝시작 기능
 */
@Service("PROJECT_START")
class ProjectStartService(
  @Autowired private val projectRepository: ProjectRepository,
) : NuguriService {

  override val minimumRequestParams = 2
  override val maximumRequestParams = 2
  override val minimumPermission = ServicePermission.MANAGER

  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    val startedProject = startProject(serviceRequestDto.params[0], serviceRequestDto.params[1])
    return "${startedProject.fullName} 프로젝트가 시작되었어요"
  }

  @Transactional
  fun startProject(name: String, nickname: String): Project {
    checkIfProjectNameExists(name)
    checkIfProjectNicknameExists(nickname)
    return projectRepository.save(Project(name, nickname))
  }

  private fun checkIfProjectNameExists(name: String) {
    if (projectRepository.existsByName(name)) {
      throw NuguriException(ExceptionType.PROJECT_NAME_ALREADY_EXISTS)
    }
  }

  private fun checkIfProjectNicknameExists(nickname: String) {
    if (projectRepository.existsByNickname(nickname)) {
      throw NuguriException(ExceptionType.PROJECT_NICKNAME_ALREADY_EXISTS)
    }
  }

}