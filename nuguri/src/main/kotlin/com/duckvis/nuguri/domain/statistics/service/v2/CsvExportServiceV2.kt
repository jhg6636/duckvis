package com.duckvis.nuguri.domain.statistics.service.v2

import com.duckvis.core.domain.nuguri.*
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.WorkTimeDto
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.statistics.NuguriCsvExportRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.CsvOption
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.core.utils.monthEndTime
import com.duckvis.core.utils.monthStartTime
import com.duckvis.nuguri.domain.attendance.service.WorkTimeService
import com.duckvis.nuguri.domain.statistics.dtos.SalaryResponseDto
import com.duckvis.nuguri.domain.statistics.dtos.SeyeobResponseDto
import com.duckvis.nuguri.external.mail.NuguriMailSender
import com.duckvis.nuguri.repository.AttendanceCardNuguriRepository
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.nio.file.Paths

@Service("CSV_EXPORT_V2")
class CsvExportServiceV2(
  private val attendanceCardNuguriRepository: AttendanceCardNuguriRepository,
  private val userRepository: UserRepository,
  private val userTeamRepository: UserTeamRepository,
  private val teamRepository: TeamRepository,
  private val userProfileRepository: UserProfileRepository,
  private val nuguriMailSender: NuguriMailSender,
  private val workTimeService: WorkTimeService,
  private val attendanceCardRepository: AttendanceCardRepository,
  private val projectRepository: ProjectRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.CSV_EXPORT

  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriCsvExportRequestParameterDto

    val email = "${params.mailId}@selectstar.ai"

    when (params.csvOption) {
      CsvOption.SALARY -> {
        val files = salaryExportCsv(params.month)
        nuguriMailSender.send(email, "${params.month}월 익스포트 CSV 파일 %기본", "", files[0])
        nuguriMailSender.send(email, "${params.month}월 익스포트 CSV 파일 %추가", "", files[1])
        files.forEach { file -> file.delete() }
      }
      CsvOption.SEYEOB -> {
        val file = seyeobExportCsv(params.month)
        nuguriMailSender.send(email, "${params.month}월 익스포트 CSV 파일", "", file)
      }
    }

    return "${email}로 익스포트 CSV 파일들을 전송했어요~"
  }

  // TODO 파일 만든다 메일 보낸다
  @Transactional
  fun salaryExportCsv(month: Int): List<File> {
    val cards = attendanceCardNuguriRepository.getAllCardsBetween(
      DateTimeMaker.nowDateTime().withMonth(month).monthStartTime,
      DateTimeMaker.nowDateTime().withMonth(month).monthEndTime
    )

    val userCards = cards.groupBy { card -> card.userId }
    val salaryResponses = userCards.map { (userId, cards) ->
      val user = userRepository.findByIdOrNull(userId) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
      val userTeams = userTeamRepository.findAllByUserId(userId)
      val userTeam = userTeams
        .filter { userTeam -> userTeam.isManager }
        .getOrNull(0)
        ?: userTeams.firstOrNull()
      val team = teamRepository.findByIdOrNull(userTeam?.teamId ?: 0L) // 팀 없는 경우 null
      val userProfile = userProfileRepository.findByUserId(userId) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)

      val workTimeDto = WorkTimeDto.of(cards)


      val notExtendedDurationSeconds = workTimeDto.all - workTimeDto.extended

      SalaryResponseDto(
        teamName = team?.name ?: "",
        salaryCode = user.salaryCode,
        isManager = userTeam?.isManager ?: false,
        name = user.fullName,
        allDurationSeconds = workTimeDto.all,
        extendedDurationSeconds = workTimeDto.extended,
        holidayDurationSeconds = workTimeDto.holiday,
        nightDurationSeconds = workTimeDto.night,
        dayOff = userProfile.dayOff,
        dayOffSick = userProfile.dayOffSick,
        overTarget = notExtendedDurationSeconds >= userProfile.targetWorkSeconds
      )
    }

    val salaryResponseGroupedByTeam = salaryResponses.groupBy { salaryResponseDto -> salaryResponseDto.teamName }

    val path = Paths.get("").toAbsolutePath().toString()
    val basicFile = File("$path/${month}월_기본.csv")
    basicFile.createNewFile()
    basicFile.writeText("팀명,이름,총근로시간,연장,휴일,야간,총근로시간-연장,총근로시간,연장,휴일,야간,총근로시간-연장,휴가 및 기타,병가,연장확인,비고\n")
    val additionalFile = File("$path/${month}월_추가.csv")
    additionalFile.createNewFile()
    additionalFile.writeText("사원코드,이름,총근로시간,연장,휴일,야간,총근로시간-연장,총근로시간,연장,휴일,야간,총근로시간-연장,휴가 및 기타,병가,연장확인,비고\n")

    salaryResponseGroupedByTeam.map { (_, salaryResponses) ->
      val salaryResponseSortedByIsManager =
        salaryResponses.sortedBy { salaryResponseDto -> salaryResponseDto.isManager }.reversed()
      salaryResponseSortedByIsManager.forEach { salaryResponseDto ->
        basicFile.appendText(salaryResponseDto.basicString)
        basicFile.appendText("\n")
      }
    }

    salaryResponses.sortedBy { salaryResponseDto -> salaryResponseDto.salaryCode }
      .forEach { salaryResponseDto ->
        additionalFile.appendText(salaryResponseDto.additionalString)
        additionalFile.appendText("\n")
      }

    return listOf(basicFile, additionalFile)
  }

  fun seyeobExportCsv(month: Int): File {
    val cards = attendanceCardNuguriRepository.getAllCardsBetween(
      DateTimeMaker.nowDateTime().withMonth(month).monthStartTime,
      DateTimeMaker.nowDateTime().withMonth(month).monthEndTime
    )
    val users = userRepository.findAll().filter { user -> !user.isGone && !user.isBot }
      .associateBy(
        { user -> user.id },
        { user ->
          workTimeService.workTime(
            user.id,
            DateTimeMaker.nowDateTime().withMonth(month).monthStartTime,
            DateTimeMaker.nowDateTime().withMonth(month).monthEndTime
          )
        }
      )
    val seyeobResponses = cards.groupBy { card -> card.projectId }
      .flatMap { (projectId, cards) ->
        val project =
          projectRepository.findByIdOrNull(projectId) ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)
        cards.groupBy { card -> card.userId }
          .map { (userId, cards) ->
            val user = userRepository.findByIdOrNull(userId) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
            val workTimeDto = workTimeService.getWorkTimeDto(cards)
            SeyeobResponseDto(
              projectName = project.name,
              startDay = cards.minOf { card -> card.loginDateTime.dayOfMonth },
              userName = user.name,
              totalSeconds = workTimeDto.all,
              nightSeconds = workTimeDto.night,
              holidaySeconds = workTimeDto.holiday,
              monthlyTotal = users[user.id]?.all ?: 0,
              monthlyExtended = users[user.id]?.extended ?: 0,
              projectExtended = workTimeDto.extended > 0
            )
          }
      }
    val path = Paths.get("").toAbsolutePath().toString()
    val file = File("$path/${month}월.csv")
    file.createNewFile()
    file.writeText("출퇴근플젝명,해당월시작일,이름,플젝전체근무시간,플젝야간근무,플젝휴일근무,월전체근무시간,월전체연장근무시간,플젝연장근무\n")
    file.writeText(seyeobResponses.joinToString("\n") { seyeobResponseDto -> seyeobResponseDto.responseString })

    return file
  }

}