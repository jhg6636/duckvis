package com.duckvis.nuguri.domain.statistics.service

import com.duckvis.core.domain.nuguri.*
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.WorkTypeDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.SpecialStatisticsType
import com.duckvis.core.types.shared.UserPathType
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.core.utils.monthEndTime
import com.duckvis.core.utils.secondsToString
import com.duckvis.nuguri.domain.attendance.dtos.MemberWorkDuration
import com.duckvis.nuguri.domain.attendance.service.WorkTimeService
import com.duckvis.nuguri.domain.statistics.dtos.AdminStatisticsRequestDto
import com.duckvis.nuguri.domain.statistics.dtos.StatisticsRequestDto
import com.duckvis.nuguri.domain.statistics.dtos.WorkTypeDuration
import com.duckvis.nuguri.repository.AttendanceCardNuguriRepository
import com.duckvis.nuguri.repository.HolidayNuguriRepository
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface StatisticsAction {

  fun act(statisticsRequestDto: StatisticsRequestDto): String {
    val cards = getCards(statisticsRequestDto)
    return message(cards, statisticsRequestDto.type)
  }

  fun isResponsible(statisticsRequestDto: StatisticsRequestDto): Boolean
  fun getCards(statisticsRequestDto: StatisticsRequestDto): List<AttendanceCard>
  fun message(cards: List<AttendanceCard>, type: SpecialStatisticsType): String

}

@Service
@Slf4j
class NormalStatisticsAction(
  @Autowired private val userRepository: UserRepository,
  @Autowired private val attendanceCardNuguriRepository: AttendanceCardNuguriRepository,
  @Autowired private val projectRepository: ProjectRepository,
  @Autowired private val workTimeService: WorkTimeService,
  @Autowired private val userProfileRepository: UserProfileRepository,
  @Autowired private val holidayNuguriRepository: HolidayNuguriRepository,
) : StatisticsAction {

  private val log = LoggerFactory.getLogger(this.javaClass)

  override fun isResponsible(statisticsRequestDto: StatisticsRequestDto): Boolean {
    return !statisticsRequestDto.isAdmin
  }

  @Transactional
  override fun getCards(statisticsRequestDto: StatisticsRequestDto): List<AttendanceCard> {
    val user =
      userRepository.findByCodeAndPath(statisticsRequestDto.userCode, UserPathType.SLACK) ?: throw NuguriException(
        ExceptionType.NO_SUCH_USER
      )
    val cards = attendanceCardNuguriRepository.getMyCardsBetween(
      user.id,
      statisticsRequestDto.startDateTime,
      statisticsRequestDto.endDateTime
    )
    val project = if (statisticsRequestDto.projectName != null) {
      projectRepository.findByNameOrNickname(
        statisticsRequestDto.projectName,
        statisticsRequestDto.projectName
      ) ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)
    } else {
      null
    }
    return cards.filter { card ->
      (project == null || card.projectId == project.id) &&
        (statisticsRequestDto.workType == WorkTypeDto() || card.isSameWorkType(statisticsRequestDto.workType))
    }
  }

  @Transactional
  override fun message(cards: List<AttendanceCard>, type: SpecialStatisticsType): String {
    val head = when (type) {
      SpecialStatisticsType.MONTHLY -> "어디 봅시다...\n이번달 통계에요~\n"
      SpecialStatisticsType.WEEKLY -> "음... 찾았어요!\n이번주 통계에요~\n"
      SpecialStatisticsType.NORMAL -> "어...앗!\n해당 기간의 통계에요~\n"
      SpecialStatisticsType.ALL_PROJECT -> "잠시만요...!\n플젝별 통계에요~\n"
    }
    if (cards.isEmpty()) {
      return ":clock12:해당 기간에 일한 기록이 없어요!"
    }
    val user = userRepository.findByIdOrNull(cards[0].userId) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val leftTime = if (type == SpecialStatisticsType.MONTHLY) {
      leftTimeMessage(user, cards)
    } else {
      ""
    }
    if (type == SpecialStatisticsType.ALL_PROJECT) {
      return "$head\n${user.name}님은\n\n${groupByProject(cards)}\n\n일하셨네요~$leftTime"
    }
    return "$head:pushpin:${user.name}님은 전체 프로젝트에서\n총 ${workTimeService.getWorkTimeDto(cards).workTimeString}\n일하셨네요~$leftTime"
  }

  private fun groupByProject(cards: List<AttendanceCard>): String {
    // projectId -> List<AttendanceCard>
    val group = cards.groupBy { card ->
      card.projectId
    }

    val strings = group.map { (projectId, groupedCards) ->
      val project = projectRepository.findByIdOrNull(projectId) ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)
      ":pushpin: ${project.name} 프로젝트에서\n총 ${workTimeService.getWorkTimeDto(groupedCards)}"
    }

    return strings.joinToString("\n")
  }

  @Transactional
  fun leftTimeMessage(user: User, cards: List<AttendanceCard>): String {
    val userProfile = userProfileRepository.findByUserId(user.id) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val doneTime = cards.sumBy { card -> card.durationSeconds ?: 0 }
    val targetSeconds = userProfile.targetWorkSeconds
    log.info("leftDay {}, targetSeconds {}, doneTime {}", leftDays(), targetSeconds, doneTime)
    return "\n이번 달 근무시간까지 ${(targetSeconds - doneTime).secondsToString}만큼 남았어요.\n" +
      "앞으로 하루에 ${((targetSeconds - doneTime) / leftDays()).secondsToString}씩 일하시면 되겠습니다~\n" +
      "힘내세요~"
  }

  @Transactional
  fun leftDays(): Int {
    val holidays =
      holidayNuguriRepository.getHolidays(
        DateTimeMaker.nowDate(),
        DateTimeMaker.nowDateTime().monthEndTime.toLocalDate()
      )
        .map { it.dayOfMonth }
    val leftDays = mutableListOf<Int>()
    for (day in DateTimeMaker.nowDate().dayOfMonth..DateTimeMaker.nowDate().lengthOfMonth()) {
      if (!holidays.contains(day)) {
        leftDays.add(day)
      }
    }

    return leftDays.size
  }

}

@Service
class AdminStatisticsAction(
  @Autowired private val attendanceCardNuguriRepository: AttendanceCardNuguriRepository,
  @Autowired private val userTeamRepository: UserTeamRepository,
  @Autowired private val teamRepository: TeamRepository,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val projectRepository: ProjectRepository,
  @Autowired private val workTimeService: WorkTimeService,
) : StatisticsAction {

  override fun isResponsible(statisticsRequestDto: StatisticsRequestDto): Boolean {
    return statisticsRequestDto.isAdmin && statisticsRequestDto.adminStatisticsRequestDto.isAdminStatistics
  }

  @Transactional
  override fun getCards(statisticsRequestDto: StatisticsRequestDto): List<AttendanceCard> {
    val user =
      userRepository.findByCodeAndPath(statisticsRequestDto.userCode, UserPathType.SLACK) ?: throw NuguriException(
        ExceptionType.NO_SUCH_USER
      )
    if (!(user.isAdmin || statisticsRequestDto.userTeam?.isManager == true)) {
      throw NuguriException(ExceptionType.NO_PERMISSION)
    }
    val cards = attendanceCardNuguriRepository.getAllCardsBetween(
      statisticsRequestDto.startDateTime,
      statisticsRequestDto.endDateTime
    )
      .filter { card ->
        checkableUserIds(
          statisticsRequestDto.userCode,
          statisticsRequestDto.userTeam,
          statisticsRequestDto.adminStatisticsRequestDto
        ).contains(card.userId)
      }
    val adminStatisticsRequestDto = statisticsRequestDto.adminStatisticsRequestDto
    if (adminStatisticsRequestDto.isEverybody) {
      return cards
    }
    return cards
      .filter { card ->
        isRightCard(card, adminStatisticsRequestDto)
      }
  }

  private fun isRightCard(card: AttendanceCard, adminStatisticsRequestDto: AdminStatisticsRequestDto): Boolean {
    return isSameProject(card, adminStatisticsRequestDto.projectName) &&
      isSameTeam(card, adminStatisticsRequestDto.teamName) &&
      isSameMember(card, adminStatisticsRequestDto.memberName)
  }

  @Transactional
  fun checkableUserIds(
    userCode: String,
    userTeam: UserTeam?,
    adminStatisticsRequestDto: AdminStatisticsRequestDto
  ): List<Long> {
    val user = userRepository.findByCodeAndPath(userCode, UserPathType.SLACK)
      ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    return when {
      user.isAdmin -> userRepository.findAll().filter { user -> !user.isGone }.map { user -> user.id }
      isManager(userTeam) -> {
        if (adminStatisticsRequestDto.teamName != null) {
          if (userTeam?.teamId != teamRepository.findByName(adminStatisticsRequestDto.teamName)?.id ?: throw NuguriException(
              ExceptionType.NO_SUCH_TEAM
            )
          ) {
            throw NuguriException(ExceptionType.NO_PERMISSION)
          }
        }
        userTeamRepository.findAllByTeamId(userTeam?.teamId ?: -1)
          .map { userTeam -> userTeam.userId }
          .filter { userId ->
            val user = userRepository.findByIdOrNull(userId) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
            !user.isAdmin
          }
      }
      else -> throw NuguriException(ExceptionType.NO_PERMISSION)
    }
  }

  private fun isManager(userTeam: UserTeam?): Boolean {
    return userTeam?.isManager ?: false
  }

  @Transactional
  fun isSameProject(card: AttendanceCard, projectName: String?): Boolean {
    if (projectName == null) {
      return true
    }
    return card.projectId == projectRepository.findByNameOrNickname(projectName, projectName)?.id
  }

  @Transactional
  fun isSameTeam(card: AttendanceCard, teamName: String?): Boolean {
    if (teamName == null) {
      return true
    }
    val team = teamRepository.findByName(teamName) ?: throw NuguriException(ExceptionType.NO_SUCH_TEAM)
    return userTeamRepository.findAllByTeamId(team.id).any { userTeam -> userTeam.isSameUser(card.userId) }
  }

  @Transactional
  fun isSameMember(card: AttendanceCard, memberName: String?): Boolean {
    if (memberName == null) {
      return true
    }
    return card.userId == userRepository.findByName(memberName)?.id
  }

  override fun message(cards: List<AttendanceCard>, type: SpecialStatisticsType): String {
    val memberWorkDurations = sortCards(cards)
    val head = when (type) {
      SpecialStatisticsType.MONTHLY -> "어디 봅시다...\n이번달 통계에요~\n"
      SpecialStatisticsType.WEEKLY -> "음... 찾았어요!\n이번주 통계에요~\n"
      SpecialStatisticsType.NORMAL -> "어...앗!\n해당 기간의 통계에요~\n"
      SpecialStatisticsType.ALL_PROJECT -> "잠시만요...!\n플젝별 통계에요~\n"
    }

    if (type == SpecialStatisticsType.ALL_PROJECT) {
      val userId = cards[0].userId
      val user = userRepository.findByIdOrNull(userId) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
      if (cards.any { card -> card.userId != userId }) {
        return "^모든플젝 옵션은 ^^^사원명 옵션과 함께 사용해 주세요~"
      }
      return "$head\n${user.name}님은\n\n${
        groupByProject(cards)
      }\n\n일하셨어요~"
    }

    return "$head\n${
      memberWorkDurations.joinToString("\n") { memberWorkDuration ->
        memberWorkDuration.oneMemberString
      }
    }\n일하셨어요~"
  }

  private fun groupByProject(cards: List<AttendanceCard>): String {
    // projectId -> List<AttendanceCard>
    val group = cards.groupBy { card ->
      card.projectId
    }

    val strings = group.map { (projectId, groupedCards) ->
      val project = projectRepository.findByIdOrNull(projectId) ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)
      ":pushpin: ${project.name} 프로젝트에서\n총 ${workTimeService.getWorkTimeDto(groupedCards)}"
    }

    return strings.joinToString("\n")
  }

  fun sortCards(cards: List<AttendanceCard>): List<MemberWorkDuration> {
    // userId -> List<WorkTypeDuration>
    val allUserDurations = cards
      .groupBy(
        { card -> card.userId },
        { card ->
          WorkTypeDuration(
            WorkTypeDto(card.isNight, card.isExtended, card.isHoliday),
            card.durationSeconds ?: 0
          )
        }
      )

    return allUserDurations.map { (userId, workTypeDurations) ->
      val userName = userRepository.findByIdOrNull(userId)?.name ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
      MemberWorkDuration.of(userName, workTypeDurations)
    }
  }

}