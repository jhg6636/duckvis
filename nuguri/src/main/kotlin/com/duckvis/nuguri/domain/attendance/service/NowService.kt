package com.duckvis.nuguri.domain.attendance.service

import com.duckvis.core.domain.nuguri.ProjectRepository
import com.duckvis.core.domain.nuguri.SubProjectRepository
import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.nuguri.domain.attendance.dtos.NowResponse
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.repository.AttendanceCardNuguriRepository
import com.duckvis.nuguri.repository.SubProjectNuguriRepository
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * 지금 기능
 */
@Service("NOW")
class NowService(
  @Autowired private val attendanceCardNuguriRepository: AttendanceCardNuguriRepository,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val projectRepository: ProjectRepository,
  @Autowired private val subProjectRepository: SubProjectRepository,
  @Autowired private val teamRepository: TeamRepository,
  @Autowired private val userTeamRepository: UserTeamRepository,
  @Autowired private val subProjectNuguriRepository: SubProjectNuguriRepository,
) : NuguriService {

  override val minimumRequestParams: Int
    get() = 0
  override val maximumRequestParams: Int
    get() = 2
  override val minimumPermission: ServicePermission
    get() = ServicePermission.MEMBER

  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    val projectSubproject = serviceRequestDto.params
      .singleOrNull { param ->
        param.startsWith("^") && !param.startsWith("^^")
      }
      ?.split("_")
    val projectName = projectSubproject?.first()?.substringAfter("^")
    val subProjectName = projectSubproject?.getOrNull(1)
    val teamName = serviceRequestDto.params.singleOrNull { param ->
      param.startsWith("^^")
    }
    val nowList = now(projectName, teamName, subProjectName)
    return if (nowList.isEmpty()) {
      "지금 아무도 일하고 있지 않아요~"
    } else {
      "지금\n${nowList.joinToString("\n") { nowResponse -> nowResponse.nowString }}\n열심히 일하고 있어요~"
    }
  }

  @Transactional
  fun now(projectName: String?, teamName: String?, subProjectName: String?): List<NowResponse> {
    val project = if (projectName != null) {
      projectRepository.findByNameOrNickname(projectName, projectName)
        ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)
    } else {
      null
    }
    val team = if (teamName != null) {
      teamRepository.findByName(teamName.substringAfter("^^")) ?: throw NuguriException(ExceptionType.NO_SUCH_TEAM)
    } else {
      null
    }
    val subProject = subProjectNuguriRepository.getSubProject(subProjectName, project?.id)
    return attendanceCardNuguriRepository.getNowWorkingCards()
      .filter { card ->
        (project == null || card.projectId == project.id) &&
          (team == null || userTeamRepository.findByUserIdAndTeamId(card.userId, team.id) != null) &&
          (subProject == null || card.subProjectId == subProject.id)
      }
      .map { card ->
        NowResponse(
          userName = userRepository.findByIdOrNull(card.userId)?.name
            ?: throw NuguriException(ExceptionType.NO_SUCH_USER),
          projectName = projectRepository.findByIdOrNull(card.projectId)?.name
            ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT),
          subProjectName = subProjectRepository.findByIdOrNull(card.subProjectId ?: 0L)?.name
        )
      }
      .sortedBy { nowResponse ->
        nowResponse.userName
      }
  }

}