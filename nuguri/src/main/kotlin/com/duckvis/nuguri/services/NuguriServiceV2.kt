package com.duckvis.nuguri.services

import com.duckvis.core.dtos.nuguri.service.params.NuguriAdminRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.NuguriGeneralRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.NuguriServiceRequestNoParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.holiday.NuguriSetHolidaysRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.holiday.NuguriSetWorkdaysRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.project.*
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.team.*
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.user.*
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMajorType
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.attendance.NuguriAttendanceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.attendance.NuguriNowRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.general.NuguriHelpRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.statistics.NuguriCsvExportRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.statistics.NuguriStatisticsRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.statistics.NuguriThisMonthInfoRequestParameterDto
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.nuguri.dtos.ServiceRequestDtoV2

interface NuguriServiceV2 {

  val type: CommandMinorType

  fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String
  fun parameterCheck(requestParameterDto: NuguriServiceRequestParameterDto) {
    val isThrow = when (type) {
      CommandMinorType.HELP -> requestParameterDto !is NuguriHelpRequestParameterDto
      CommandMinorType.LOGIN, CommandMinorType.LOGOUT, CommandMinorType.MISTAKE -> requestParameterDto !is NuguriAttendanceRequestParameterDto
      CommandMinorType.SET_HOLIDAYS -> requestParameterDto !is NuguriSetHolidaysRequestParameterDto
      CommandMinorType.SET_WORKDAYS -> requestParameterDto !is NuguriSetWorkdaysRequestParameterDto
      CommandMinorType.ADD_SUB_PROJECT -> requestParameterDto !is NuguriAddSubProjectRequestParameterDto
      CommandMinorType.DELETE_SUB_PROJECT -> requestParameterDto !is NuguriDeleteSubProjectRequestParameterDto
      CommandMinorType.PROJECT_END -> requestParameterDto !is NuguriProjectEndRequestParameterDto
      CommandMinorType.PROJECT_START -> requestParameterDto !is NuguriProjectStartRequestParameterDto
      CommandMinorType.SUB_PROJECT_LIST -> requestParameterDto !is NuguriSubProjectListRequestParameterDto
      CommandMinorType.ADD_MANAGER -> requestParameterDto !is NuguriAddManagerRequestParameterDto
      CommandMinorType.ADD_TEAM_MEMBER -> requestParameterDto !is NuguriAddTeamMemberRequestParameterDto
      CommandMinorType.ADD_TEAM -> requestParameterDto !is NuguriAddTeamRequestParameterDto
      CommandMinorType.CHANGE_TEAM_NAME -> requestParameterDto !is NuguriChangeTeamNameRequestParameterDto
      CommandMinorType.DELETE_MANAGER -> requestParameterDto !is NuguriDeleteManagerRequestParameterDto
      CommandMinorType.DELETE_TEAM_MEMBER -> requestParameterDto !is NuguriDeleteTeamMemberRequestParameterDto
      CommandMinorType.DELETE_TEAM -> requestParameterDto !is NuguriDeleteTeamRequestParameterDto
      CommandMinorType.TEAM_INFO -> requestParameterDto !is NuguriTeamInfoRequestParameterDto
      CommandMinorType.ADD_ADMIN -> requestParameterDto !is NuguriAddAdminRequestParameterDto
      CommandMinorType.CHANGE_PROFILE -> requestParameterDto !is NuguriChangeProfileRequestParameterDto
      CommandMinorType.DELETE_ADMIN -> requestParameterDto !is NuguriDeleteAdminRequestParameterDto
      CommandMinorType.MEMBER_EXIT -> requestParameterDto !is NuguriMemberExitRequestParameterDto
      CommandMinorType.SET_DAY_OFF -> requestParameterDto !is NuguriSetDayOffRequestParameterDto
      CommandMinorType.SET_SICK_DAY_OFF -> requestParameterDto !is NuguriSetSickDayOffRequestParameterDto
      CommandMinorType.SHOW_USER_PROFILE -> requestParameterDto !is NuguriShowUserProfileRequestParameterDto
      CommandMinorType.NOW -> requestParameterDto !is NuguriNowRequestParameterDto
      CommandMinorType.CSV_EXPORT -> requestParameterDto !is NuguriCsvExportRequestParameterDto
      CommandMinorType.STATISTICS -> requestParameterDto !is NuguriStatisticsRequestParameterDto
      CommandMinorType.THIS_MONTH_INFO -> requestParameterDto !is NuguriThisMonthInfoRequestParameterDto
      else -> requestParameterDto !is NuguriServiceRequestNoParameterDto
    }

    if (isThrow) {
      throw NuguriException(ExceptionType.TYPO)
    }
  }

}