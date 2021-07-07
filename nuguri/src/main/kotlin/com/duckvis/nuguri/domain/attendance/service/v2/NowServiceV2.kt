package com.duckvis.nuguri.domain.attendance.service.v2

import com.duckvis.core.domain.nuguri.ProjectRepository
import com.duckvis.core.domain.nuguri.SubProjectRepository
import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.attendance.NuguriNowRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.nuguri.domain.attendance.dtos.NowResponse
import com.duckvis.nuguri.repository.AttendanceCardNuguriRepository
import com.duckvis.nuguri.repository.SubProjectNuguriRepository
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service("NOW_V2")
class NowServiceV2(
  private val attendanceCardNuguriRepository: AttendanceCardNuguriRepository,
  private val userRepository: UserRepository,
  private val projectRepository: ProjectRepository,
  private val subProjectRepository: SubProjectRepository,
  private val teamRepository: TeamRepository,
  private val userTeamRepository: UserTeamRepository,
  private val subProjectNuguriRepository: SubProjectNuguriRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.NOW

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriNowRequestParameterDto
    val nowList = now(params.projectName, params.teamName, params.subProjectName)
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