package com.duckvis.nuguri.ui

import com.duckvis.core.domain.nuguri.UserTeam
import com.duckvis.core.domain.shared.User
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.service.CommandMajorType
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.core.types.nuguri.service.NuguriServicePermission
import com.duckvis.core.types.nuguri.service.params.*

class CommandParserV2(
) {

  /**
   * split된 text 받아 RequestParam parsing
   */
  fun extractRequestParams(
    splitText: List<String>,
    commandMinorType: CommandMinorType,
    userName: String,
    userCode: String,
    userTeam: UserTeam,
  ): NuguriServiceRequestParameterDto {
    commandMinorType.paramNumberCheck(splitText)
    return when (commandMinorType.commandMajorType) {
      CommandMajorType.ADMIN -> {
        NuguriAdminRequestParameterDto.of(splitText, userName, userCode, userName)
      }
      CommandMajorType.ATTENDANCE -> {
        NuguriAttendanceRequestParameterDto.of(splitText, userCode, userName)
      }
      CommandMajorType.STATISTICS -> {
        NuguriStatisticsRequestParameterDto.of(splitText, userTeam, userCode, userName)
      }
      CommandMajorType.GENERAL -> {
        NuguriGeneralRequestParameterDto.of(splitText)
      }
    }
  }

  fun checkPermission(commandMinorType: CommandMinorType, userTeam: UserTeam, user: User) {
    val myPermission = when {
      user.isAdmin -> NuguriServicePermission.ADMIN
      userTeam.isManager -> NuguriServicePermission.MANAGER
      user.isGone -> NuguriServicePermission.EVERYBODY
      else -> NuguriServicePermission.MEMBER
    }
    if (!myPermission.available(commandMinorType.basePermission)) {
      throw NuguriException(ExceptionType.NO_PERMISSION)
    }
  }

}