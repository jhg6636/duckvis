package com.duckvis.nuguri.services

import com.duckvis.core.domain.nuguri.Team
import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeam
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.UserTeamLevel
import com.duckvis.nuguri.domain.admin.service.team.MyTeamService
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
class MyTeamServiceTest(
  @Autowired private val myTeamService: MyTeamService,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val teamRepository: TeamRepository,
  @Autowired private val userTeamRepository: UserTeamRepository,
) {
  @BeforeEach
  @Transactional
  fun prepare() {
    userRepository.save(User("user", "user"))
    val manager = userRepository.save(User("manager", "manager"))
    val team1 = teamRepository.save(Team("team1"))
    val team2 = teamRepository.save(Team("team2"))
    userTeamRepository.save(UserTeam(manager.id, team1.id, UserTeamLevel.MANAGER))
    userTeamRepository.save(UserTeam(manager.id, team2.id, UserTeamLevel.MANAGER))
  }

  @AfterEach
  @Transactional
  fun clear() {
    userTeamRepository.deleteAllInBatch()
    teamRepository.deleteAllInBatch()
    userRepository.deleteAllInBatch()
  }

  @Transactional
  fun addToTeam(userName: String, teamName: String) {
    val user = userRepository.findByName(userName) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val team = teamRepository.findByName(teamName) ?: throw NuguriException(ExceptionType.NO_SUCH_TEAM)

    userTeamRepository.save(UserTeam(user.id, team.id))
  }

  @Test
  fun `1개 팀에 속해 있을 때 내 팀 정보를 확인한다`() {
    // given
    addToTeam("user", "team1")

    // when
    val teamInformation = myTeamService.myTeam("user")

    // then
    assertThat(teamInformation).hasSize(1)
    println(teamInformation[0])
  }

  @Test
  fun `2개 팀에 속해 있을 때 내 팀 정보를 확인한다`() {
    // given
    addToTeam("user", "team1")
    addToTeam("user", "team2")

    // when
    val teamInformations = myTeamService.myTeam("user")

    // then
    assertThat(teamInformations).hasSize(2)
    println(teamInformations[0])
    println(teamInformations[1])
  }
}