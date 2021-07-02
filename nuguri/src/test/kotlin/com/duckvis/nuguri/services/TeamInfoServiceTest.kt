package com.duckvis.nuguri.services

import com.duckvis.core.domain.nuguri.Team
import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeam
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.types.nuguri.UserTeamLevel
import com.duckvis.nuguri.domain.admin.service.team.TeamInfoService
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
class TeamInfoServiceTest(
  @Autowired private val teamInfoService: TeamInfoService,
  @Autowired private val teamRepository: TeamRepository,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val userTeamRepository: UserTeamRepository,
) {
  @BeforeEach
  @Transactional
  fun prepare() {
    val user1 = userRepository.save(User("team1user", "team1user"))
    val manager1 = userRepository.save(User("team1manager", "team1manager"))
    val user2 = userRepository.save(User("team2user", "team2user"))
    val manager2 = userRepository.save(User("team2manager1", "team2manager1"))
    val manager3 = userRepository.save(User("team2manager2", "team2manager2"))

    val team1 = teamRepository.save(Team("team1"))
    val team2 = teamRepository.save(Team("team2"))

    userTeamRepository.save(UserTeam(user1.id, team1.id))
    userTeamRepository.save(UserTeam(manager1.id, team1.id, UserTeamLevel.MANAGER))
    userTeamRepository.save(UserTeam(user2.id, team2.id))
    userTeamRepository.save(UserTeam(manager2.id, team2.id, UserTeamLevel.MANAGER))
    userTeamRepository.save(UserTeam(manager3.id, team2.id, UserTeamLevel.MANAGER))
  }

  @AfterEach
  @Transactional
  fun clear() {
    userTeamRepository.deleteAllInBatch()
    teamRepository.deleteAllInBatch()
    userRepository.deleteAllInBatch()
  }

  @Test
  fun `team2에 대한 정보를 확인한다`() {
    // given

    // when
    val teamInformation = teamInfoService.teamInformation("team2")
    val managerNames = teamInformation.manager.map { it.name }
    val memberNames = teamInformation.members.map { it.name }

    // then
    assertThat(teamInformation.manager).hasSize(2)
    assertThat(managerNames).contains("team2manager1", "team2manager2")
    assertThat(teamInformation.members).hasSize(3)
    assertThat(memberNames).contains("team2manager1", "team2manager2", "team2user")
    assertThat(teamInformation.name).isEqualTo("team2")
  }
}