package com.duckvis.nuguri.ui

import com.duckvis.core.domain.nuguri.UserTeam
import com.duckvis.core.domain.shared.User
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.NuguriServiceRequestNoParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.holiday.NuguriSetHolidaysRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.holiday.NuguriSetWorkdaysRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.project.*
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.team.*
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.user.*
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.attendance.NuguriAttendanceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.attendance.NuguriNowRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.general.NuguriHelpRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.statistics.NuguriCoreTimeStatisticsRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.statistics.NuguriCsvExportRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.statistics.NuguriStatisticsRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.statistics.NuguriThisMonthInfoRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.HelpOption
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.core.types.nuguri.service.NuguriServicePermission

class CommandParserV2(
) {

  /**
   * split된 text 받아 RequestParam parsing
   */
  fun extractRequestParams(
    splitText: List<String>,
    userName: String,
    userCode: String,
    userTeam: UserTeam,
    isAdmin: Boolean,
    isGone: Boolean,
  ): NuguriServiceRequestParameterDto {
    val commandMinorType = CommandMinorType.of(splitText.firstOrNull() ?: "")
    commandMinorType?.paramNumberCheck(splitText)
    if (commandMinorType == null) {
      return NuguriServiceRequestNoParameterDto(userCode, userName)
    }
    checkPermission(commandMinorType, userTeam, isAdmin, isGone)
    return when (commandMinorType) {
      CommandMinorType.HELP -> NuguriHelpRequestParameterDto(
        userCode,
        userName,
        HelpOption.of(splitText.getOrNull(1) ?: "")
      )
      CommandMinorType.LOGIN, CommandMinorType.LOGOUT, CommandMinorType.MISTAKE -> NuguriAttendanceRequestParameterDto.of(
        splitText, userCode, userName
      )
      CommandMinorType.NOW -> NuguriNowRequestParameterDto.of(splitText, userCode, userName)
      CommandMinorType.STATISTICS -> NuguriStatisticsRequestParameterDto.of(splitText, userTeam, userCode, userName)
      CommandMinorType.CORE_TIME_STATISTICS -> NuguriCoreTimeStatisticsRequestParameterDto.of(splitText, userCode, userName)
      CommandMinorType.CSV_EXPORT -> NuguriCsvExportRequestParameterDto.of(splitText, userCode, userName)
      CommandMinorType.SET_HOLIDAYS -> NuguriSetHolidaysRequestParameterDto.of(splitText, userCode, userName)
      CommandMinorType.SET_WORKDAYS -> NuguriSetWorkdaysRequestParameterDto.of(splitText, userCode, userName)
      CommandMinorType.THIS_MONTH_INFO -> NuguriThisMonthInfoRequestParameterDto.of(splitText, userCode, userName)
      CommandMinorType.ADD_ADMIN -> NuguriAddAdminRequestParameterDto(userCode, userName, splitText[1])
      CommandMinorType.DELETE_ADMIN -> NuguriDeleteAdminRequestParameterDto(userCode, userName, splitText[1])
      CommandMinorType.MEMBER_EXIT -> NuguriMemberExitRequestParameterDto(userCode, userName, splitText[1])
      CommandMinorType.CHANGE_PROFILE -> NuguriChangeProfileRequestParameterDto(userCode, userName, splitText[1], splitText[2])
      CommandMinorType.SET_DAY_OFF -> NuguriSetDayOffRequestParameterDto(userCode, userName, splitText[1].toInt())
      CommandMinorType.SET_SICK_DAY_OFF -> NuguriSetSickDayOffRequestParameterDto(userCode, userName, splitText[1].toInt())
      CommandMinorType.SHOW_USER_PROFILE -> NuguriShowUserProfileRequestParameterDto.of(splitText, userCode, userName)
      CommandMinorType.ADD_SUB_PROJECT -> NuguriAddSubProjectRequestParameterDto(userCode, userName, splitText[1], splitText[2], splitText[3])
      CommandMinorType.DELETE_SUB_PROJECT -> NuguriDeleteSubProjectRequestParameterDto(userCode, userName, splitText[1], splitText[2])
      CommandMinorType.PROJECT_END -> NuguriProjectEndRequestParameterDto(userCode, userName, splitText[1])
      CommandMinorType.PROJECT_START -> NuguriProjectStartRequestParameterDto(userCode, userName, splitText[1], splitText[2])
      CommandMinorType.SUB_PROJECT_LIST -> NuguriSubProjectListRequestParameterDto(userCode, userName, splitText[1])
      CommandMinorType.ADD_MANAGER -> NuguriAddManagerRequestParameterDto(userCode, userName, splitText[1], splitText[2])
      CommandMinorType.ADD_TEAM_MEMBER -> NuguriAddTeamMemberRequestParameterDto(userCode, userName, splitText[1], splitText[2])
      CommandMinorType.ADD_TEAM -> NuguriAddTeamRequestParameterDto(userCode, userName, splitText[1], splitText[2])
      CommandMinorType.CHANGE_TEAM_NAME -> NuguriChangeTeamNameRequestParameterDto(userCode, userName, splitText[1], splitText[2])
      CommandMinorType.DELETE_MANAGER -> NuguriDeleteManagerRequestParameterDto(userCode, userName, splitText[1], splitText[2])
      CommandMinorType.DELETE_TEAM_MEMBER -> NuguriDeleteTeamMemberRequestParameterDto(userCode, userName, splitText[1], splitText[2])
      CommandMinorType.DELETE_TEAM -> NuguriDeleteTeamRequestParameterDto(userCode, userName, splitText[1])
      CommandMinorType.TEAM_INFO -> NuguriTeamInfoRequestParameterDto(userCode, userName, splitText[1])
      else -> NuguriServiceRequestNoParameterDto(userCode, userName)
    }
  }

  private fun checkPermission(commandMinorType: CommandMinorType, userTeam: UserTeam, isAdmin: Boolean, isGone: Boolean) {
    val myPermission = when {
      isAdmin -> NuguriServicePermission.ADMIN
      userTeam.isManager -> NuguriServicePermission.MANAGER
      isGone -> NuguriServicePermission.EVERYBODY
      else -> NuguriServicePermission.MEMBER
    }
    if (!myPermission.available(commandMinorType.basePermission)) {
      throw NuguriException(ExceptionType.NO_PERMISSION)
    }
  }

}