package com.duckvis.nuguri.services

import com.duckvis.core.domain.nuguri.Team
import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeam
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.nuguri.domain.admin.service.team.AddManagerService
import com.duckvis.nuguri.utils.AssertNuguriException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
class AddManagerServiceTest(
  @Autowired private val addManagerService: AddManagerService,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val userTeamRepository: UserTeamRepository,
  @Autowired private val teamRepository: TeamRepository,
) {
  @BeforeEach
  @Transactional
  fun prepare() {
    val user1 = userRepository.save(User("user1", "user1"))
    val user2 = userRepository.save(User("user2", "user2"))
    val user3 = userRepository.save(User("user3", "user3"))
    val team1 = teamRepository.save(Team("team1"))
    val team2 = teamRepository.save(Team("team2"))
    userTeamRepository.save(UserTeam(user1.id, team1.id))
    userTeamRepository.save(UserTeam(user2.id, team1.id))
    userTeamRepository.save(UserTeam(user3.id, team2.id))
  }

  @AfterEach
  @Transactional
  fun clear() {
    userTeamRepository.deleteAll()
    teamRepository.deleteAll()
    userRepository.deleteAll()
  }

  @Test
  fun `팀원 중 한 명을 팀 매니저로 지정한다`() {
    // given

    // when
    val userTeam = addManagerService.addManager("team1", "user1")

    // then
    assertThat(userTeam.isManager).isTrue
  }

  @Test
  fun `팀원이 아닌 사람을 팀 매니저로 지정한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.NOT_TEAM_MEMBER).assert {
      addManagerService.addManager("team1", "user3")
    }
  }

  @Test
  fun `팀 매니저가 있는데, 다른 팀원을 팀 매니저로 지정한다`() {
    // given
    addManagerService.addManager("team1", "user1")
    val team = teamRepository.findByName("team1") ?: throw NuguriException(ExceptionType.NO_SUCH_TEAM)

    // when
    addManagerService.addManager("team1", "user2")
    val userTeams = userTeamRepository.findAllByTeamId(team.id)
    val managerNames = userTeams.filter { it.isManager }.mapNotNull { userRepository.findByIdOrNull(it.userId)?.name }

    // then
    assertThat(userTeams).hasSize(2)
    assertThat(managerNames).hasSize(2)
    assertThat(managerNames).contains("user1")
    assertThat(managerNames).contains("user2")
  }

  @Test
  fun `이미 매니저인 사람을 매니저로 지정한다`() {
    // given
    addManagerService.addManager("team1", "user1")

    // when & then
    AssertNuguriException(ExceptionType.ALREADY_TEAM_MANAGER).assert {
      addManagerService.addManager("team1", "user1")
    }
  }

  @Test
  fun `사원이 아닌 사람을 매니저로 지정한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.NO_SUCH_USER).assert {
      addManagerService.addManager("team1", "user5")
    }
  }

  @Test
  fun `모르는 팀에 매니저를 추가한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.NO_SUCH_TEAM).assert {
      addManagerService.addManager("team3", "user3")
    }
  }
}