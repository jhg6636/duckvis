package com.duckvis.nuguri.domain.admin.service.project

import com.duckvis.core.domain.nuguri.ProjectRepository
import com.duckvis.core.domain.nuguri.SubProjectRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * !서브플젝제거
 */
@Service("DELETE_SUB_PROJECT")
class DeleteSubProjectService(
  private val projectRepository: ProjectRepository,
  private val subProjectRepository: SubProjectRepository,
) : NuguriService {

  override val maximumRequestParams: Int
    get() = 2
  override val minimumRequestParams: Int
    get() = 2
  override val minimumPermission: ServicePermission
    get() = ServicePermission.MANAGER

  @Transactional
  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    val project = projectRepository.findByNameOrNickname(serviceRequestDto.params[1], serviceRequestDto.params[1])
      ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)
    val subProject = subProjectRepository.findByNameAndProjectId(serviceRequestDto.params[0], project.id)
      ?: throw NuguriException(ExceptionType.NO_SUCH_SUB_PROJECT)

    subProject.deleteSubProject()
    return "${project.name} 플젝의 ${subProject.name} 서브플젝이 제거되었어요~"
  }

}