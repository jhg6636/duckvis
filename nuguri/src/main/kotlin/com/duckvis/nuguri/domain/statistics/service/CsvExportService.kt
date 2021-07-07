package com.duckvis.nuguri.domain.statistics.service

import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserProfileRepository
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.WorkTimeDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.CsvOption
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.core.utils.monthEndTime
import com.duckvis.core.utils.monthStartTime
import com.duckvis.nuguri.domain.statistics.dtos.SalaryResponseDto
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.external.mail.NuguriMailSender
import com.duckvis.nuguri.repository.AttendanceCardNuguriRepository
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.nio.file.Paths

/**
 * 어드민의 !익스포트 기능 구현 (csv 형식의 스트링으로 출퇴근 전체 목록 익스포트)
 */

@Service("CSV_EXPORT")
class CsvExportService(
  @Autowired private val attendanceCardNuguriRepository: AttendanceCardNuguriRepository,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val userTeamRepository: UserTeamRepository,
  @Autowired private val teamRepository: TeamRepository,
  @Autowired private val userProfileRepository: UserProfileRepository,
  @Autowired private val nuguriMailSender: NuguriMailSender,
) : NuguriService {

  override val minimumRequestParams: Int // csv 옵션, 메일아이디 (selectstar.ai), 몇월? - 몇월 없을 경우 지난달 것
    get() = 2
  override val maximumRequestParams: Int
    get() = 3
  override val minimumPermission: ServicePermission
    get() = ServicePermission.ADMIN

  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)

    val month = serviceRequestDto.params.getOrNull(2)?.toInt() ?: DateTimeMaker.nowDate().monthValue - 1
    if (month > 12 || month < 1) {
      throw NuguriException(ExceptionType.MONTH_TYPO)
    }

    val email = "${serviceRequestDto.params[1]}@selectstar.ai"

    return when (CsvOption.of(serviceRequestDto.params)) {
      CsvOption.SALARY -> {
        val files = salaryExportCsv(month)
        nuguriMailSender.send(email, "${month}월 익스포트 CSV 파일 %기본", "", files[0])
        nuguriMailSender.send(email, "${month}월 익스포트 CSV 파일 %추가", "", files[1])
        files.forEach { file -> file.delete() }
        "${email}로 익스포트 CSV 파일들을 전송했어요~"
      }
      CsvOption.SEYEOB -> ""
    }
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
        additionalFile.appendText(salaryResponseDto.additionalString)
      }

    return listOf(basicFile, additionalFile)
  }

}