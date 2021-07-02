package com.duckvis.nuguri.services

import com.duckvis.core.domain.nuguri.UserTeam
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.shared.UserLevelType
import com.duckvis.nuguri.dtos.ServiceRequestDto

interface NuguriService {
  val maximumRequestParams: Int
  val minimumRequestParams: Int
  val minimumPermission: ServicePermission

  fun response(serviceRequestDto: ServiceRequestDto): String

  fun isValidCommand(serviceRequestDto: ServiceRequestDto) {
    checkPermission(getPermission(serviceRequestDto.userLevel, serviceRequestDto.userTeam), serviceRequestDto.userLevel)
    checkTypo(serviceRequestDto)
  }

  fun checkPermission(permission: ServicePermission, userLevel: UserLevelType) {
    if (minimumPermission != ServicePermission.EVERYBODY && userLevel.isExit) {
      throw NuguriException(ExceptionType.USER_EXIT)
    }
    if (permission < minimumPermission) {
      throw NuguriException(ExceptionType.NO_PERMISSION)
    }
  }

  fun checkTypo(serviceRequestDto: ServiceRequestDto) {
    if (serviceRequestDto.params.size > maximumRequestParams || serviceRequestDto.params.size < minimumRequestParams) {
      throw NuguriException(ExceptionType.TYPO)
    }
  }

  fun getPermission(userLevel: UserLevelType, userTeam: UserTeam?): ServicePermission {
    return when {
      userLevel.isAdmin -> ServicePermission.ADMIN
      !userLevel.isAdmin && (userTeam == null || !userTeam.isManager) -> ServicePermission.MEMBER
      !userLevel.isAdmin && (userTeam != null && userTeam.isManager) -> ServicePermission.MANAGER
      else -> ServicePermission.EVERYBODY
    }
  }

  fun isAboutAttendance(): Boolean {
    return false
  }
}

// ordinal
enum class ServicePermission {
  EVERYBODY,
  MEMBER,
  MANAGER,
  ADMIN;
}