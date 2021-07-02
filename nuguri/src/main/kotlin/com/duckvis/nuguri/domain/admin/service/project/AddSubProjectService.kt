package com.duckvis.nuguri.domain.admin.service.project

import com.duckvis.core.domain.nuguri.ProjectRepository
import com.duckvis.core.domain.nuguri.SubProject
import com.duckvis.core.domain.nuguri.SubProjectRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * !서브플젝추가 기능
 */
@Service("ADD_SUB_PROJECT")
class AddSubProjectService(
  private val subProjectRepository: SubProjectRepository,
  private val projectRepository: ProjectRepository,
) : NuguriService {

  override val maximumRequestParams = 3
  override val minimumRequestParams = 3
  override val minimumPermission = ServicePermission.MANAGER

  @Transactional
  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    if (subProjectRepository.existsByNameOrNickname(serviceRequestDto.params[0], serviceRequestDto.params[1])) {
      throw NuguriException(ExceptionType.SUB_PROJECT_NAME_OR_NICKNAME_ALREADY_EXISTS)
    }
    val parentProject = projectRepository.findByNameOrNickname(serviceRequestDto.params[2], serviceRequestDto.params[2])
      ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)
    val subProject =
      subProjectRepository.save(SubProject(parentProject.id, serviceRequestDto.params[0], serviceRequestDto.params[1]))

    return "${subProject.name} 서브플젝이 ${parentProject.name} 프로젝트 아래에 생성되었어요~"
  }


}