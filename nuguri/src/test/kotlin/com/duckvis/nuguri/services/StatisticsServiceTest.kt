package com.duckvis.nuguri.services

import com.duckvis.core.domain.User
import com.duckvis.core.domain.UserRepository
import com.duckvis.core.utils.TimeHandler
import com.duckvis.nuguri.exceptions.NotWorkingException
import com.duckvis.nuguri.types.Gender
import com.duckvis.nuguri.types.UserTeamLevel
import com.duckvis.nuguri.domain.*
import com.duckvis.nuguri.services.Statistics
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
@ActiveProfiles("test")
class StatisticsServiceTest constructor(
    @Autowired private val attendanceCardRepository: AttendanceCardRepository,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val teamRepository: TeamRepository,
    @Autowired private val userTeamRepository: UserTeamRepository,
    @Autowired private val projectRepository: ProjectRepository,
    @Autowired private val statistics: Statistics,
    @Autowired private val userProfileRepository: UserProfileRepository,
) {
    lateinit var user: User
    lateinit var userProfile: UserProfile
    lateinit var manager: User
    lateinit var project1: Project
    lateinit var project2: Project
    lateinit var team: Team

    @BeforeEach
    fun prepare() {
        user = userRepository.save(User("member", "member"))
        userProfile =
            userProfileRepository.save(
                UserProfile(
                    user.id,
                    user.name,
                    Gender.MALE,
                    LocalDate.of(1997, 12, 29),
                    LocalDate.of(2020, 7, 1),
                    "hi",
                    176
                )
            )
        manager = userRepository.save(User("manager", "manager"))
        team = teamRepository.save(Team("team"))
        userTeamRepository.save(UserTeam(user.id, team.id, UserTeamLevel.MEMBER))
        userTeamRepository.save(UserTeam(manager.id, team.id, UserTeamLevel.MANAGER))

        project1 = projectRepository.save(Project("기본", "ㄱㅂ"))
        project2 = projectRepository.save(Project("대출갚기", "ㄷㅊㄱㄱ"))
    }

    @AfterEach
    fun clean() {
        userTeamRepository.deleteAll()
        userRepository.deleteAll()
        teamRepository.deleteAll()
        projectRepository.deleteAll()
    }

    private fun loginBeforeHours(user: User, project: Project, hours: Long): AttendanceCard {
        val loginCard = AttendanceCard(user.id, project.id, CardType.WORK, TimeHandler.nowDateTime().minusHours(hours))
        return attendanceCardRepository.save(loginCard)
    }

    private fun makeAttendanceCardBeforeDays(user: User, project: Project, days: Long): AttendanceCard {
        val card = AttendanceCard(
            user.id,
            project.id,
            CardType.WORK,
            TimeHandler.nowDateTime().minusDays(days).minusHours(3),
            10800,
            TimeHandler.nowDateTime().minusDays(days)
        )
        return attendanceCardRepository.save(card)
    }

    @Test
    fun `현재 출근해 있는 사람이 몇시간을 입력한다`() {
        // given
        loginBeforeHours(user, project1, 3)

        // when
        val howLongIWorkedResponse = statistics.responseHowLongIWorked(user.id)

        // then
        assertThat(howLongIWorkedResponse.nowWorkSeconds).isEqualTo(10800)
        assertThat(howLongIWorkedResponse.todayWorkSeconds).isEqualTo(10800)
    }

    @Test
    fun `현재 출근해 있지 않은 사람이 몇시간을 입력한다`() {
        // given

        // when & then
        assertThrows<NotWorkingException> {
            statistics.responseHowLongIWorked(user.id)
        }
    }

    @Test
    fun `주간통계를 입력한다`() {
        // given
        makeAttendanceCardBeforeDays(user, project1, 0)

        // when
        val weeklyWorkHour =
            statistics.responseStatistics(user.id, TimeHandler.weekStartTime(), TimeHandler.weekEndTime())

        // then
        assertThat(weeklyWorkHour).isEqualTo(10800)
    }

    @Test
    fun `시작날짜와 끝날짜를 포함하여 통계를 확인한다`() {
        // given
        makeAttendanceCardBeforeDays(user, project1, 2)
        makeAttendanceCardBeforeDays(user, project2, 1)
        makeAttendanceCardBeforeDays(user, project1, 0)
        val today = TimeHandler.nowDateTime()

        // when
        val workHour = statistics.responseStatistics(user.id, today.minusDays(2), today.plusDays(1))

        // then
        assertThat(workHour).isEqualTo(32400)

    }

    @Test
    fun `특정 날짜의 통계를 입력한다`() {
        // given
        makeAttendanceCardBeforeDays(user, project1, 35)

        // when
        val workHour = statistics.responseStatistics(user.id, TimeHandler.nowDateTime().minusDays(35))

        // then
        assertThat(workHour).isEqualTo(10800)
    }

    @Test
    @Transactional
    fun `2명이 출근해 있는데 지금을 입력한다`() {
        // given
        val userCard = loginBeforeHours(user, project1, 2)
        val managerCard = loginBeforeHours(manager, project2, 5)

        // when
        val attendanceCards = statistics.responseNow()

        // then
        assertThat(attendanceCards).hasSize(2)
        assertThat(attendanceCards).contains(userCard)
        assertThat(attendanceCards).contains(managerCard)
    }

    @Test
    fun `팀장이 플젝에 참여한 모든 사람들의 근무시간을 확인한다`() {
        // given
        makeAttendanceCardBeforeDays(user, project1, 5)
        makeAttendanceCardBeforeDays(manager, project1, 4)

        // when
        val map = statistics.responseAdminStatistics(
            project1.id,
            null,
            null,
            TimeHandler.nowDateTime().minusDays(7),
            TimeHandler.nowDateTime()
        )

        // then
        assertThat(map.entries).hasSize(2)
        assertThat(map[user.id]).isEqualTo(10800)
        assertThat(map[manager.id]).isEqualTo(10800)
    }

    @Test
    fun `팀장이 팀 멤버들 전체의 근무시간을 확인한다`() {
        // given
        makeAttendanceCardBeforeDays(user, project1, 5)
        makeAttendanceCardBeforeDays(user, project1, 4)
        makeAttendanceCardBeforeDays(user, project1, 3)
        makeAttendanceCardBeforeDays(manager, project1, 2)
        makeAttendanceCardBeforeDays(manager, project1, 1)

        // when
        val map = statistics.responseAdminStatistics(
            null,
            team.id,
            null,
            TimeHandler.nowDateTime().minusDays(7),
            TimeHandler.nowDateTime()
        )

        // then
        assertThat(map.entries).hasSize(2)
        assertThat(map[user.id]).isEqualTo(32400)
        assertThat(map[manager.id]).isEqualTo(21600)
    }

    @Test
    fun `팀장이 특정 사원의 근무시간을 확인한다`() {
        // given
        makeAttendanceCardBeforeDays(user, project1, 5)
        makeAttendanceCardBeforeDays(user, project2, 4)
        makeAttendanceCardBeforeDays(user, project2, 3)
        makeAttendanceCardBeforeDays(user, project2, 2)

        // when
        val map = statistics.responseAdminStatistics(
            null,
            null,
            user.id,
            TimeHandler.nowDateTime().minusDays(7),
            TimeHandler.nowDateTime()
        )

        // then
        assertThat(map.entries).hasSize(1)
        assertThat(map[user.id]).isEqualTo(43200)
    }

    @Test
    fun `마지막 퇴근 시간을 확인한다`() {
        // given
        makeAttendanceCardBeforeDays(user, project1, 5)
        makeAttendanceCardBeforeDays(user, project2, 1)

        // when
        val lastLogOut = statistics.responseLastLogOut(user.id)

        // then
        assertThat(lastLogOut).isNotNull
        assertThat(lastLogOut!!.projectId).isEqualTo(project2.id)
        assertThat(lastLogOut!!.logoutDateTime).isBetween(
            TimeHandler.dayStartTime(TimeHandler.nowDateTime().minusDays(1)),
            TimeHandler.dayEndTime(TimeHandler.nowDateTime().minusDays(1))
        )
        assertThat(lastLogOut!!.durationSeconds).isEqualTo(10800)
    }
}