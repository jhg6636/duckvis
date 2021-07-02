package com.duckvis.nuguri.domain.attendance.service

import com.duckvis.core.domain.nuguri.*
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.Work
import com.duckvis.core.dtos.nuguri.WorkTypeDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.shared.UserPathType
import com.duckvis.nuguri.domain.attendance.dtos.LogInResponseDto
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.repository.AttendanceCardNuguriRepository
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * 출근을 담당하는 서비스
 */
@Service("LOGIN")
class LogInService(
  @Autowired private val attendanceCardRepository: AttendanceCardRepository,
  @Autowired private val attendanceCardNuguriRepository: AttendanceCardNuguriRepository,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val projectRepository: ProjectRepository,
  @Autowired private val subProjectRepository: SubProjectRepository,
) : NuguriService {

  override val minimumRequestParams: Int
    get() = 0
  override val maximumRequestParams: Int
    get() = 4
  override val minimumPermission: ServicePermission
    get() = ServicePermission.MEMBER

  override fun checkTypo(serviceRequestDto: ServiceRequestDto) {
    super.checkTypo(serviceRequestDto)
    if ((serviceRequestDto.params - extractOptions(serviceRequestDto.params)).size > 1) {
      throw NuguriException(ExceptionType.TYPO)
    }
  }

  // @Trx
  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)

    // 1. 일단 기존의 AttendacneCard?를 가져온다
    // 2. 기존재하는 AttendanceCard가 있다면 바로 exception을 날린다

    // 3. Work 대신 AttendanceCard를 바로 깔끔하게 만들고 저장 (이 과정에서 User, Project, SubProject?, AttendanceCard)
    // 4. 아까 가져왔던 기존의 AttendanceCard를 logout 처리 해준다


    // THINKING
    val work = makeWork(serviceRequestDto.params, serviceRequestDto.userCode)

    val loginResponse = logIn(work)
    return loginResponse.responseString
  }

  private fun makeWork(requestParams: List<String>, userCode: String): Work {
    val user = userRepository.findByCodeAndPath(userCode, UserPathType.SLACK)
      ?: throw NuguriException(ExceptionType.NO_SUCH_USER)

    when (requestParams.size) {
      0 -> {
        val basicProject =
          projectRepository.findByNameOrNickname("기본", "ㄱㅂ") ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)
        return Work(user.id, basicProject.id, null)
      }
      1, 2, 3, 4 -> {
        val options = extractOptions(requestParams)
        val projectSubProject = (requestParams - options).singleOrNull()?.split("_") // [플젝명]_[서브플젝명] split한 것
          ?: throw NuguriException(ExceptionType.TYPO)
        val project = extractProject(projectSubProject)
        val subProjectNameOrNickname = projectSubProject.getOrNull(1)
          ?: return Work(user.id, project.id, null, WorkTypeDto.of(options))

        val subProjectId = extractSubProjectId(subProjectNameOrNickname, project)

        return Work(user.id, project.id, subProjectId, WorkTypeDto.of(options))
      }
      else -> throw NuguriException(ExceptionType.TYPO)
    }
  }

  @Transactional
  fun logIn(work: Work): LogInResponseDto {
    val userName = userRepository.findByIdOrNull(work.userId)?.name
      ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val projectName = projectRepository.findByIdOrNull(work.projectId)?.fullName
      ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)


    val subProjectName = subProjectRepository.findByIdOrNull(work.subProjectId ?: 0L)?.fullName
    val card = AttendanceCard(work)
    val logOutProject = logOutBeforeProject(work)
    attendanceCardRepository.save(card)

    return LogInResponseDto(userName, projectName, logOutProject, subProjectName)
  }

  /**
   * 이전 출근해 있던 프로젝트 퇴근처리
   * @return 이전 출근해 있던 프로젝트 fullName
   */
  @Transactional
  fun logOutBeforeProject(work: Work): String? {
    val nowWorkingCard = attendanceCardNuguriRepository.getMyWorkingCard(work.userId) ?: return null
    nowWorkingCard.checkAlreadyWork(work)
    nowWorkingCard.logOut()

    return projectRepository.findByIdOrNull(nowWorkingCard.projectId)?.fullName
      ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)
  }

  private fun extractProject(projectSubProject: List<String>): Project {
    val projectNameOrNickName = projectSubProject.first()
    return projectRepository.findByNameOrNickname(projectNameOrNickName, projectNameOrNickName)
      ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)
  }

  private fun extractSubProjectId(subProjectNameOrNickname: String, project: Project): Long {
    return subProjectRepository.findByNameAndProjectId(subProjectNameOrNickname, project.id)?.id
      ?: subProjectRepository.findByNicknameAndProjectId(subProjectNameOrNickname, project.id)?.id
      ?: throw NuguriException(ExceptionType.NO_SUCH_SUB_PROJECT)
  }

  private fun extractOptions(requestParams: List<String>): List<String> {
    return requestParams.filter { param -> param.startsWith("%") }
  }

  override fun isAboutAttendance(): Boolean {
    return true
  }

}