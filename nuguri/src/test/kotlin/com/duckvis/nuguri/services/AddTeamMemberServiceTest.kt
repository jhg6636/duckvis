package com.duckvis.nuguri.services

import com.duckvis.core.domain.nuguri.Team
import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.nuguri.domain.admin.service.team.AddTeamMemberService
import com.duckvis.nuguri.utils.AssertNuguriException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
class AddTeamMemberServiceTest(
  @Autowired private val addTeamMemberService: AddTeamMemberService,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val teamRepository: TeamRepository,
  @Autowired private val userTeamRepository: UserTeamRepository,
) {
  @BeforeEach
  @Transactional
  fun prepare() {
    userRepository.save(User("user1", "user1"))
    userRepository.save(User("user2", "user2"))
    teamRepository.save(Team("team1"))
    teamRepository.save(Team("team2"))
  }

  @AfterEach
  @Transactional
  fun clear() {
    userTeamRepository.deleteAll()
    teamRepository.deleteAll()
    userRepository.deleteAll()
  }

  @Test
  fun `팀에 팀원을 추가한다`() {
    // given
    val user1 = userRepository.findByName("user1")
    val team1 = teamRepository.findByName("team1")

    // when
    val userTeam = addTeamMemberService.addTeamMember("user1", "team1")

    // then
    assertThat(userTeam.userId).isEqualTo(user1?.id)
    assertThat(userTeam.teamId).isEqualTo(team1?.id)
    assertThat(userTeam.isManager).isFalse
  }

  @Test
  fun `사원이 아닌 사람을 팀원으로 추가한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.NO_SUCH_USER).assert {
      addTeamMemberService.addTeamMember("user3", "team2")
    }
  }

  @Test
  fun `한 팀의 팀원인 사람을 다른 팀의 팀원으로 추가한다`() {
    // given
    addTeamMemberService.addTeamMember("user1", "team1")
    val user = userRepository.findByName("user1") ?: throw NuguriException(ExceptionType.NO_SUCH_USER)

    // when
    addTeamMemberService.addTeamMember("user1", "team2")
    val userTeams = userTeamRepository.findAllByUserId(user.id)

    // then
    assertThat(userTeams).hasSize(2)
  }

  @Test
  fun `모르는 팀에 팀원을 추가하려 한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.NO_SUCH_TEAM).assert {
      addTeamMemberService.addTeamMember("user1", "team5")
    }
  }
}