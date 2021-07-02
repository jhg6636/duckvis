package com.duckvis.nuguri.domain.statistics.service

import com.duckvis.core.dtos.nuguri.WorkTypeDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.SpecialStatisticsType
import com.duckvis.core.types.shared.UserLevelType
import com.duckvis.nuguri.domain.statistics.dtos.AdminStatisticsRequestDto
import com.duckvis.nuguri.domain.statistics.dtos.StatisticsRequestDto
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * 모든 통계 기능 (관리자통계, 일반사원 통계 + 월간/주간 통계)
 */
@Slf4j
@Service("STATISTICS")
class StatisticsService(
  @Autowired private val statisticsAction: List<StatisticsAction>,
) : NuguriService {

  private val log = LoggerFactory.getLogger(this.javaClass)

  override val minimumRequestParams: Int
    get() = 1
  override val maximumRequestParams: Int
    get() = 8
  override val minimumPermission: ServicePermission
    get() = ServicePermission.MEMBER

  override fun checkTypo(serviceRequestDto: ServiceRequestDto) {
    super.checkTypo(serviceRequestDto)
    if ((serviceRequestDto.text.startsWith("!월간통계") ||
        serviceRequestDto.text.startsWith("!ㅇㄱㅌㄱ") ||
        serviceRequestDto.text.startsWith("!주간통계") ||
        serviceRequestDto.text.startsWith("!ㅈㄱㅌㄱ")) &&
      (serviceRequestDto.params.size != 2 && !isAdminStatistics(serviceRequestDto))
    ) {
      log.info("${serviceRequestDto.params.size}")
      throw NuguriException(ExceptionType.TYPO)
    } else if (!isProperParams(serviceRequestDto.params)) {
      throw NuguriException(ExceptionType.TYPO)
    }
  }

  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    val statisticsRequestDto = getRequestDto(serviceRequestDto)
    statisticsAction.forEach {
      if (it.isResponsible(statisticsRequestDto)) {
        return it.act(statisticsRequestDto)
      }
    }
    throw NuguriException(ExceptionType.TYPO)
  }

  /**
   * 입력된 파라미터가 유효한지 검사
   */
  private fun isProperParams(params: List<String>): Boolean {
    return params.all { param ->
      param.all { char -> char.isDigit() } ||
        listOf("%야간", "%휴일", "%연장").contains(param) ||
        (param.startsWith("^") && !param.startsWith("^^^^"))
    } && params.any { param ->
      param.all { char -> char.isDigit() }
    }
  }

  private fun getRequestDto(serviceRequestDto: ServiceRequestDto): StatisticsRequestDto {
    val isAdmin = isAdminStatistics(serviceRequestDto)
    val type = SpecialStatisticsType.of(serviceRequestDto.text)
    val dateTimes = serviceRequestDto.dateTimes

    val workTypeStrings = serviceRequestDto.params.filter { param -> param.startsWith("%") }
    val workTypeDto = WorkTypeDto.of(workTypeStrings)

    val projectName = extractProjectName(serviceRequestDto.params)
    val adminStatisticsRequestDto = AdminStatisticsRequestDto.of(serviceRequestDto.params)

    return StatisticsRequestDto(
      serviceRequestDto.userCode,
      serviceRequestDto.userTeam,
      isAdmin,
      type,
      dateTimes[0],
      dateTimes[1],
      workTypeDto,
      projectName,
      adminStatisticsRequestDto
    )
  }

  private fun isAdminStatistics(serviceRequestDto: ServiceRequestDto): Boolean {
    if (serviceRequestDto.userLevel != UserLevelType.ADMIN && (serviceRequestDto.userTeam != null && !serviceRequestDto.userTeam.isManager)) {
      return false
    }
    if (!AdminStatisticsRequestDto.of(serviceRequestDto.params).isAdminStatistics) {
      return false
    }
    return serviceRequestDto.params.any { param -> param.startsWith("^^") || param == "^전체" }
  }

  private fun extractProjectName(requestParams: List<String>): String? {
    return requestParams.singleOrNull { it.startsWith("^") && !it.startsWith("^^") }
  }

}