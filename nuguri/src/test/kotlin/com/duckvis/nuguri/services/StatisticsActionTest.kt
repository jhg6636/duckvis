package com.duckvis.nuguri.services

import com.duckvis.core.domain.nuguri.*
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.Work
import com.duckvis.core.dtos.nuguri.WorkTypeDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.SpecialStatisticsType
import com.duckvis.core.types.nuguri.UserTeamLevel
import com.duckvis.core.types.shared.UserLevelType
import com.duckvis.core.utils.StartAndEndTime
import com.duckvis.core.utils.dayEndTime
import com.duckvis.core.utils.dayStartTime
import com.duckvis.nuguri.domain.attendance.dtos.MemberWorkDuration
import com.duckvis.nuguri.domain.statistics.dtos.AdminStatisticsRequestDto
import com.duckvis.nuguri.domain.statistics.dtos.StatisticsRequestDto
import com.duckvis.nuguri.domain.statistics.service.AdminStatisticsAction
import com.duckvis.nuguri.domain.statistics.service.NormalStatisticsAction
import com.duckvis.nuguri.utils.AssertNuguriException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
class StatisticsActionTest(
  @Autowired private val normalStatisticsAction: NormalStatisticsAction,
  @Autowired private val adminStatisticsAction: AdminStatisticsAction,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val attendanceCardRepository: AttendanceCardRepository,
  @Autowired private val projectRepository: ProjectRepository,
  @Autowired private val teamRepository: TeamRepository,
  @Autowired private val userTeamRepository: UserTeamRepository,
) {
  lateinit var managerUserTeam: UserTeam
  lateinit var adminUserTeam: UserTeam
  lateinit var user1: User
  lateinit var user2: User

  @BeforeEach
  @Transactional
  fun prepare() {
    user1 = userRepository.save(User("user1", "user1"))
    user2 = userRepository.save(User("user2", "user2"))

    val user3 = userRepository.save(User("user3", "user3"))
    val manager = userRepository.save(User("manager", "manager"))
    val admin = userRepository.save(User("admin", "admin", level = UserLevelType.ADMIN))

    val team1 = teamRepository.save(Team("team1"))
    val team2 = teamRepository.save(Team("team2"))

    userTeamRepository.save(UserTeam(user1.id, team1.id))
    managerUserTeam = userTeamRepository.save(UserTeam(manager.id, team1.id, UserTeamLevel.MANAGER))
    userTeamRepository.save(UserTeam(user2.id, team2.id))
    userTeamRepository.save(UserTeam(user3.id, team2.id))
    adminUserTeam = userTeamRepository.save(UserTeam(admin.id, team2.id, UserTeamLevel.MANAGER))

    projectRepository.save(Project("project1", "p1"))
    projectRepository.save(Project("project2", "p2"))
    projectRepository.save(Project("project3", "p3"))
  }

  @AfterEach
  @Transactional
  fun clear() {
    attendanceCardRepository.deleteAllInBatch()
    userTeamRepository.deleteAllInBatch()
    teamRepository.deleteAllInBatch()
    userRepository.deleteAllInBatch()
    projectRepository.deleteAllInBatch()
  }

  @Transactional
  fun attendBetween(
    userName: String,
    projectNickname: String,
    startTime: LocalDateTime,
    endTime: LocalDateTime,
    workType: WorkTypeDto = WorkTypeDto()
  ): AttendanceCard {
    val user = userRepository.findByName(userName) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val project = projectRepository.findByNameOrNickname(projectNickname, projectNickname) ?: throw NuguriException(
      ExceptionType.NO_SUCH_PROJECT
    )
    val work = Work(user.id, project.id, null, workType)
    val card = AttendanceCard(work, startTime, Duration.between(startTime, endTime).toSeconds().toInt(), endTime)

    return attendanceCardRepository.save(card)
  }

  private fun makeStatisticsRequestDto(
    userCode: String,
    userTeam: UserTeam?,
    type: SpecialStatisticsType,
    start: LocalDateTime,
    end: LocalDateTime,
    workType: WorkTypeDto,
    projectName: String?,
    isAdmin: Boolean = false,
    adminStatisticsRequestDto: AdminStatisticsRequestDto = AdminStatisticsRequestDto()
  ): StatisticsRequestDto {
    return StatisticsRequestDto(
      userCode,
      userTeam,
      isAdmin,
      type,
      start,
      end,
      workType,
      projectName,
      adminStatisticsRequestDto
    )
  }

  @Test
  fun `2시간 일한 사람이 통계를 확인한다`() {
    // given
    attendBetween(
      "user1",
      "p1",
      StartAndEndTime.coreTimeStart.dayStartTime.plusHours(5),
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(7)
    )
    val statisticsRequestDto = makeStatisticsRequestDto(
      "user1",
      null,
      SpecialStatisticsType.NORMAL,
      StartAndEndTime.coreTimeStart.dayStartTime,
      StartAndEndTime.coreTimeEnd.dayEndTime,
      WorkTypeDto(),
      null
    )

    // when
    val cards = normalStatisticsAction.getCards(statisticsRequestDto)

    // then
    assertThat(cards).hasSize(1)
    assertThat(cards[0].durationSeconds).isEqualTo(7200)
    assertThat(cards[0].isSameWorkType(WorkTypeDto())).isTrue
    assertThat(cards[0].isNowWorking).isFalse
    assertThat(cards[0].isWorkCard).isTrue
  }

  @Test
  fun `기본 근무로 4시간, 연장 근무로 3시간 일한 사람이 연장 근무 통계를 확인한다`() {
    // given
    attendBetween(
      "user1",
      "p1",
      StartAndEndTime.coreTimeStart.dayStartTime.plusHours(5),
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(7)
    )
    attendBetween(
      "user1",
      "p1",
      StartAndEndTime.coreTimeStart.dayStartTime.plusHours(9),
      StartAndEndTime.coreTimeStart.dayStartTime.plusHours(11)
    )
    attendBetween(
      "user1",
      "p1",
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(14),
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(17),
      WorkTypeDto(isExtended = true)
    )
    val statisticsRequestDto = makeStatisticsRequestDto(
      "user1",
      null,
      SpecialStatisticsType.NORMAL,
      StartAndEndTime.coreTimeStart.dayStartTime,
      StartAndEndTime.coreTimeStart.dayEndTime,
      WorkTypeDto(isExtended = true),
      null
    )

    // when
    val cards = normalStatisticsAction.getCards(statisticsRequestDto)

    // then
    assertThat(cards).hasSize(1)
    assertThat(cards[0].isWorkCard).isTrue
    assertThat(cards[0].isNowWorking).isFalse
    assertThat(cards[0].isSameWorkType(WorkTypeDto(isExtended = true))).isTrue
    assertThat(cards[0].durationSeconds).isEqualTo(10800)
  }

  @Test
  fun `p1에서 2시간, p2에서 5시간 일한 사람이 p2 통계를 확인한다`() {
    // given
    attendBetween(
      "user1",
      "p1",
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(5),
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(7)
    )
    attendBetween(
      "user1",
      "p2",
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(9),
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(11)
    )
    attendBetween(
      "user1",
      "p2",
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(14),
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(17),
      WorkTypeDto()
    )
    val statisticsRequestDto = makeStatisticsRequestDto(
      "user1",
      null,
      SpecialStatisticsType.NORMAL,
      StartAndEndTime.coreTimeStart.dayStartTime,
      StartAndEndTime.coreTimeStart.dayEndTime,
      WorkTypeDto(),
      "p2"
    )

    // when
    val cards = normalStatisticsAction.getCards(statisticsRequestDto)

    // then
    assertThat(cards).hasSize(2)
    assertThat(cards.sumBy { card -> card.durationSeconds ?: 0 }).isEqualTo(18000)
  }

  // 관리자 통계
  @Test
  fun `팀 매니저가 팀원들의 통계를 확인한다`() {
    // given
    attendBetween(
      "user1",
      "p1",
      StartAndEndTime.coreTimeStart.dayStartTime.plusHours(5),
      StartAndEndTime.coreTimeStart.dayStartTime.plusHours(6)
    )
    attendBetween(
      "user2",
      "p1",
      StartAndEndTime.coreTimeStart.dayStartTime.plusHours(5),
      StartAndEndTime.coreTimeStart.dayStartTime.plusHours(7)
    )
    attendBetween(
      "user3",
      "p1",
      StartAndEndTime.coreTimeStart.dayStartTime.plusHours(5),
      StartAndEndTime.coreTimeStart.dayStartTime.plusHours(8)
    )
    val statisticsRequestDto = makeStatisticsRequestDto(
      "manager",
      managerUserTeam,
      SpecialStatisticsType.NORMAL,
      StartAndEndTime.coreTimeEnd.dayStartTime,
      StartAndEndTime.coreTimeEnd.dayEndTime,
      WorkTypeDto(),
      null,
      true,
      AdminStatisticsRequestDto(teamName = "team1")
    )

    // when
    val cards = adminStatisticsAction.getCards(statisticsRequestDto)

    // then
    assertThat(cards).hasSize(1)
    assertThat(cards[0].durationSeconds).isEqualTo(3600)
  }

  @Test
  fun `팀 매니저가 플젝 통계를 확인한다`() {
    // given
    attendBetween(
      "user1",
      "p1",
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(5),
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(6)
    )
    attendBetween(
      "user2",
      "p1",
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(5),
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(7)
    )
    attendBetween(
      "user3",
      "p2",
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(5),
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(8)
    )
    val statisticsRequestDto = makeStatisticsRequestDto(
      "manager",
      managerUserTeam,
      SpecialStatisticsType.NORMAL,
      StartAndEndTime.coreTimeEnd.dayStartTime,
      StartAndEndTime.coreTimeEnd.dayEndTime,
      WorkTypeDto(),
      "p2",
      true,
      AdminStatisticsRequestDto(projectName = "p2")
    )

    // when
    val cards = adminStatisticsAction.getCards(statisticsRequestDto)

    // then
    assertThat(cards).hasSize(0)
  }

  @Test
  fun `팀 매니저가 다른 팀의 통계를 확인한다`() {
    // given
    attendBetween(
      "user1",
      "p1",
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(5),
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(6)
    )
    attendBetween(
      "user2",
      "p1",
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(5),
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(7)
    )
    val statisticsRequestDto = makeStatisticsRequestDto(
      "manager",
      managerUserTeam,
      SpecialStatisticsType.NORMAL,
      StartAndEndTime.coreTimeStart.dayStartTime,
      StartAndEndTime.coreTimeStart.dayEndTime,
      WorkTypeDto(),
      null,
      true,
      AdminStatisticsRequestDto(teamName = "team2")
    )

    // when & then
    AssertNuguriException(ExceptionType.NO_PERMISSION).assert {
      adminStatisticsAction.getCards(statisticsRequestDto)
    }
  }

  @Test
  fun `어드민이 플젝 통계를 확인한다`() {
    // given
    attendBetween(
      "user1",
      "p1",
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(5),
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(6)
    )
    attendBetween(
      "user2",
      "p1",
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(5),
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(7)
    )
    attendBetween(
      "user3",
      "p2",
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(5),
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(8)
    )
    val statisticsRequestDto = makeStatisticsRequestDto(
      "admin",
      adminUserTeam,
      SpecialStatisticsType.NORMAL,
      StartAndEndTime.coreTimeStart.dayStartTime,
      StartAndEndTime.coreTimeStart.dayEndTime,
      WorkTypeDto(),
      "p2",
      true,
      AdminStatisticsRequestDto(projectName = "p2")
    )

    // when
    val cards = adminStatisticsAction.getCards(statisticsRequestDto)

    // then
    assertThat(cards).hasSize(1)
    assertThat(cards[0].durationSeconds).isEqualTo(10800)
  }

  @Test
  fun `어드민이 특정 사원의 통계를 확인한다`() {
    attendBetween(
      "user1",
      "p1",
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(5),
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(6)
    )
    attendBetween(
      "user2",
      "p1",
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(5),
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(7)
    )
    attendBetween(
      "user3",
      "p2",
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(5),
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(8)
    )
    val statisticsRequestDto = makeStatisticsRequestDto(
      "admin",
      adminUserTeam,
      SpecialStatisticsType.NORMAL,
      StartAndEndTime.coreTimeStart.dayStartTime,
      StartAndEndTime.coreTimeStart.dayEndTime,
      WorkTypeDto(),
      null,
      true,
      AdminStatisticsRequestDto(memberName = "user2")
    )

    // when
    val cards = adminStatisticsAction.getCards(statisticsRequestDto)

    // then
    assertThat(cards).hasSize(1)
    assertThat(cards[0].durationSeconds).isEqualTo(7200)
  }

  @Test
  fun `어드민 통계로 뽑힌 카드를 정렬한다`() {
    // given
    val work1 = Work(user1.id, 1, null)
    val work2 = Work(user2.id, 1, null)
    val card1 = AttendanceCard(
      work1,
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(5),
      2 * 3600,
      StartAndEndTime.coreTimeStart.dayStartTime.plusHours(7)
    )
    val card2 = AttendanceCard(
      work1,
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(8),
      1 * 3600,
      StartAndEndTime.coreTimeStart.dayStartTime.plusHours(9)
    )
    val card3 = AttendanceCard(
      work2,
      StartAndEndTime.coreTimeEnd.dayStartTime.plusHours(10),
      8 * 3600,
      StartAndEndTime.coreTimeStart.dayStartTime.plusHours(18)
    )

    // when
    val map = adminStatisticsAction.sortCards(listOf(card1, card2, card3))
    val duration1 = map.single { memberWorkDuration -> memberWorkDuration.userName == "user1" }
    val duration2 = map.single { memberWorkDuration -> memberWorkDuration.userName == "user2" }

    // then
    assertThat(map).hasSize(2)
    assertThat(duration1).isEqualTo(MemberWorkDuration("user1", 10800, 0, 0, 0))
    assertThat(duration2).isEqualTo(MemberWorkDuration("user2", 28800, 0, 0, 0))
  }
}