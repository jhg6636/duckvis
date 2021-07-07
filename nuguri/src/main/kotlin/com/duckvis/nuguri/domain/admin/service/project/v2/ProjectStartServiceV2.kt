package com.duckvis.nuguri.domain.admin.service.project.v2

import com.duckvis.core.domain.nuguri.Project
import com.duckvis.core.domain.nuguri.ProjectRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.project.NuguriProjectStartRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service("PROJECT_START_V2")
class ProjectStartServiceV2(
  private val projectRepository: ProjectRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.PROJECT_START

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriProjectStartRequestParameterDto
    val project = startProject(params.name, params.nickname)
    return "${project.fullName} 프로젝트가 시작되었어요"
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