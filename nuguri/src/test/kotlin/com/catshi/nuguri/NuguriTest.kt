package com.catshi.nuguri

import com.catshi.core.domain.Team
import com.catshi.core.domain.TeamRepository
import com.catshi.core.domain.User
import com.catshi.core.domain.UserRepository
import com.catshi.core.exceptions.NoSuchTeamException
import com.catshi.core.exceptions.NotTeamMemberException
import com.catshi.core.exceptions.TeamMemberAlreadyExistsException
import com.catshi.core.exceptions.TeamNameAlreadyExistsException
import com.catshi.core.utils.TimeHandler
import com.catshi.nuguri.domain.*
import com.catshi.nuguri.exceptions.*
import com.catshi.nuguri.services.Nuguri
import com.catshi.nuguri.types.AttendanceOption
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
open class NuguriTest @Autowired constructor(
    val attendanceCardRepository: AttendanceCardRepository,
    val attendanceCardQueryDslRepository: AttendanceCardQueryDslRepository,
    val projectRepository: ProjectRepository,
    val teamRepository: TeamRepository,
    val userRepository: UserRepository,
    val nuguri: Nuguri
) {
    lateinit var user1: User
    lateinit var user2: User
    lateinit var user3: User
    lateinit var user4: User
    lateinit var user5: User
    lateinit var user6: User

    lateinit var team1: Team
    lateinit var team2: Team
    lateinit var team3: Team

    lateinit var project1: Project
    lateinit var project2: Project
    lateinit var project3: Project

    @BeforeEach
    @Transactional
    fun prepareTest() {
        user1 = User("u1", "first user").let { userRepository.save(it) }
        user2 = User("u2", "second user").let { userRepository.save(it) }
        user3 = User("u3", "third user").let { userRepository.save(it) }
        user4 = User("u4", "fourth user").let { userRepository.save(it) }
        user5 = User("u5", "fifth user").let { userRepository.save(it) }
        user6 = User("u6", "sixth user").let { userRepository.save(it) }

        team1 = Team("first team").let { teamRepository.save(it) }
        team2 = Team("second team").let { teamRepository.save(it) }
        team3 = Team("third team").let { teamRepository.save(it) }

        project1 = Project("first project", "fp", team1.id).let { projectRepository.save(it) }
        project2 = Project("second project", "sp", team2.id).let { projectRepository.save(it) }
        project3 = Project("third project", "tp", team3.id).let { projectRepository.save(it) }
    }

    @AfterEach
    fun cleanTest() {
        userRepository.deleteAll()
        teamRepository.deleteAll()
        projectRepository.deleteAll()
        attendanceCardRepository.deleteAll()
    }

    @Transactional
    open fun attendHoursBefore(user: User, project: Project, hour: Long): AttendanceCard {
        return AttendanceCard(user.id, project.id, CardType.WORK, AttendanceOption.NORMAL, TimeHandler.nowDateTime().minusHours(hour))
            .apply { attendanceCardRepository.save(this) }
    }

    // 여기서부터 팀 및 프로젝트 등록, 삭제 관련 테스트

    @Test
    fun `현재 존재하지 않는 팀명의 팀을 등록한다`() {
        // given

        // when
        nuguri.responseAddTeam("fourth team", user1.id)

        // then
        assertThat(teamRepository.count()).isEqualTo(4)
    }

    @Test
    fun `현재 존재하는 팀명의 팀을 등록한다`() {
        // given

        // when & then
        assertThrows<TeamNameAlreadyExistsException> {
            nuguri.responseAddTeam("first team", user1.id)
        }
    }

    @Test
    fun `현재 없는 팀을 삭제한다`() {
        // given

        // when & then
        assertThrows<NoSuchTeamException> {
            nuguri.responseDeleteTeam("mystery team")
        }
    }

    @Test
    fun `현재 존재하는 팀에, 팀에 없는 팀원을 등록한다`() {
        // given

        // when
        nuguri.responseAddTeamMember(user1.id, team1.id)

        // then
        assertThat(userRepository.countByTeamId(team1.id)).isEqualTo(1)
    }

    @Test
    fun `현재 존재하는 팀에, 팀에 있는 팀원을 등록한다`() {
        // given
        user1.teamId = team1.id
        userRepository.save(user1)

        // when & then
        assertThrows<TeamMemberAlreadyExistsException> {
            nuguri.responseAddTeamMember(user1.id, team1.id)
        }
    }

    @Test
    fun `현재 존재하지 않는 팀에, 팀원 등록을 시도한다`() {
        // given
        val notExistingTeam = Team("not existing team")

        // when & then
        assertThrows<NoSuchTeamException> {
            nuguri.responseAddTeamMember(user1.id, notExistingTeam.id)
        }
    }

    @Test
    fun `현재 존재하는 팀에서, 팀에 없는 팀원을 삭제한다`() {
        // given

        // when & then
        assertThrows<NotTeamMemberException> {
            nuguri.responseDeleteTeamMember(user1.id, team1.id)
        }
    }

    @Test
    @Transactional
    fun `현재 존재하는 팀에서, 팀에 있는 팀원을 삭제한다`() {
        // given
        user1.teamId = team1.id
        user2.teamId = team1.id
        userRepository.saveAll(listOf(user1, user2))

        // when
        nuguri.responseDeleteTeamMember(user1.id, team1.id)

        // then
        assertThat(userRepository.countByTeamId(team1.id)).isEqualTo(1)
    }

    @Test
    fun `현재 존재하지 않는 팀에서, 팀원 삭제를 시도한다`() {
        // given
        val notExistingTeam = Team("not existing team")

        // when & then
        assertThrows<NoSuchTeamException> {
            nuguri.responseDeleteTeamMember(user1.id, notExistingTeam.id)
        }
    }

    @Test
    fun `현재 존재하지 않는 프로젝트를 등록한다`() {
        // given

        // when
        nuguri.responseAddProject("basic", "ㄱㅂ", team1.id)

        // then
        assertThat(projectRepository.count()).isEqualTo(4)
    }

    @Test
    fun `현재 존재하는 이름으로 프로젝트를 등록한다`() {
        // given

        // when & then
        assertThrows<ProjectNameAlreadyExistsException> {
            nuguri.responseAddProject("first project", "fpfp", team1.id)
        }
    }

    @Test
    fun `현재 존재하는 별칭으로 프로젝트를 등록한다`() {
        // given

        // when & then
        assertThrows<ProjectNicknameAlreadyExistsException> {
            nuguri.responseAddProject("asdf project", "fp", team1.id)
        }
    }

    @Test
    fun `현재 존재하지 않는 프로젝트를 삭제한다`() {
        // given
        val unknownProject = Project("unknown project", "up", team3.id)

        // when & then
        assertThrows<NoSuchProjectException> {
            nuguri.responseDeleteProject(unknownProject.id)
        }
    }

    @Test
    fun `현재 존재하는 프로젝트를 삭제한다`() {
        // given

        // when
        nuguri.responseDeleteProject(project1.id)

        // then
        assertThat(projectRepository.count()).isEqualTo(2)
    }

    @Test
    fun `프로젝트가 3개 등록되어 있는 상태에서, 전체 프로젝트 보기를 시도한다`() {
        // given

        // when & then
        assertThat(nuguri.responseShowAllProjects().size).isEqualTo(3)
    }

    // 여기서부터 출퇴근 관련 테스트

    @Test
    fun `현재 출근해 있지 않은 사람이, 출근한다`() {
        // given

        // when
        val attendanceCard = nuguri.responseLogin(user1.id, project1.id)

        // then
        assertThat(attendanceCard.durationSeconds).isEqualTo(null)
        assertThat(attendanceCard.userId).isEqualTo(user1.id)
        assertThat(attendanceCardQueryDslRepository.getMyWorkingCard(user1.id)!!.id).isEqualTo(attendanceCard.id)
    }

    @Test
    fun `현재 출근해 있는 사람이, 출근해 있는 프로젝트로 출근한다`() {
        // given
        attendHoursBefore(user1, project1, 3)

        // when & then
        assertThrows<AlreadyAttendedException> {
            nuguri.responseLogin(user1.id, project1.id)
        }
    }

    @Test
    fun `현재 출근해 있는 사람이, 출근해 있는 프로젝트와 다른 프로젝트로 출근한다`() {
        // given
        val beforeAttendanceCard = attendHoursBefore(user1, project1, 3)

        // when
        val afterAttendanceCard = nuguri.responseLogin(user1.id, project2.id)
        println("beforeAttendanceCard durationSeconds")
        println(beforeAttendanceCard.durationSeconds)

        // then
        assertThat(beforeAttendanceCard.durationSeconds).isEqualTo(10800)
        assertThat(beforeAttendanceCard.projectId).isEqualTo(project1.id)
        assertThat(afterAttendanceCard.projectId).isEqualTo(project2.id)
        assertThat(attendanceCardQueryDslRepository.getMyWorkingCard(user1.id)).isEqualTo(afterAttendanceCard.id)
    }

    @Test
    fun `현재 출근해 있지 않은 사람이, 퇴근한다`() {
        // given

        // when & then
        assertThrows<NotWorkingException> {
            nuguri.responseLogout(user1.id)
        }
    }

    @Test
    fun `현재 출근해 있는 사람이, 퇴근한다`() {
        // given
        attendHoursBefore(user1, project1, 3)

        // when
        val attendanceCard = nuguri.responseLogout(user1.id)

        // then
        assertThat(attendanceCard.durationSeconds).isEqualTo(10800)
        assertThat(attendanceCard.projectId).isEqualTo(project1.id)
        assertThat(attendanceCard.userId).isEqualTo(user1.id)
    }

    @Test
    fun `실수를 입력한다`() {
        // given

        // when
        val mistakeCard = nuguri.responseMistake(user1.id, project1.id, 30)

        // then
        assertThat(mistakeCard.durationSeconds).isEqualTo(1800)
        assertThat(mistakeCard.projectId).isEqualTo(project1.id)
        assertThat(mistakeCard.userId).isEqualTo(user1.id)
    }

    @Test
    fun `출근 기록과 실수 입력 기록이 있는 사람이, 통계를 확인한다`() {
        // given
        attendHoursBefore(user1, project1, 3)
        nuguri.responseLogout(user1.id)
        nuguri.responseMistake(user1.id, project1.id, 60)

        // when
        val seconds = nuguri.responseStatistics(user1.id)

        // then
        assertThat(seconds).isEqualTo(14400)
    }

    @Test
    fun `4명 출근해 있는데, 지금을 입력한다`() {
        // given
        attendHoursBefore(user1, project1, 1)
        attendHoursBefore(user2, project1, 2)
        attendHoursBefore(user3, project2, 3)
        attendHoursBefore(user4, project3, 4)

        // when
        val attendedList = nuguri.responseNow()

        // then
        assertThat(attendedList.size).isEqualTo(4)
    }

    @Test
    fun `3시간 일한 이후, 몇시간을 입력한다`() {
        // given
        attendHoursBefore(user1, project1, 3)

        // when
        val howLongIWorked = nuguri.responseHowLongIWorked(user1.id)

        // then
        assertThat(howLongIWorked.todayWorkSeconds).isEqualTo(10800)
        assertThat(howLongIWorked.weekWorkSeconds).isEqualTo(10800)
    }

    @Test
    fun `코어타임이 되었는데, 전 직원 6명 중 2명이 출근하지 않았다`() {
        // given
        attendHoursBefore(user1, project1, 1)
        attendHoursBefore(user2, project2, 2)
        attendHoursBefore(user3, project3, 3)
        attendHoursBefore(user4, project1, 4)

        // when
        val notWorkingList = nuguri.startCoreTime()

        // then
//        assertThat(user5.id).isIn(notWorkingList)
//        assertThat(user6.id).isIn(notWorkingList)
        assertThat(notWorkingList.size).isEqualTo(2)
    }
}