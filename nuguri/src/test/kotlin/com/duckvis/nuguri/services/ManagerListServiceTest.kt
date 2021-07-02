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
import com.duckvis.nuguri.domain.admin.service.team.ManagerListService
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
class ManagerListServiceTest(
  @Autowired private val managerListService: ManagerListService,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val teamRepository: TeamRepository,
  @Autowired private val userTeamRepository: UserTeamRepository,
) {
  @BeforeEach
  @Transactional
  fun prepare() {
    userRepository.save(User("user", "user"))
    teamRepository.save(Team("team"))
  }

  @AfterEach
  @Transactional
  fun clear() {
    userTeamRepository.deleteAllInBatch()
    teamRepository.deleteAllInBatch()
    userRepository.deleteAllInBatch()
  }

  @Transactional
  fun makeManager(userName: String, teamName: String) {
    val user = userRepository.findByName(userName) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val team = teamRepository.findByName(teamName) ?: throw NuguriException(ExceptionType.NO_SUCH_TEAM)

    val userTeam = userTeamRepository.save(UserTeam(user.id, team.id, UserTeamLevel.MANAGER))
  }

  @Test
  fun `매니저가 없을 때 매니지목록을 입력한다`() {
    // given

    // when
    val userTeams = managerListService.getManagers()

    // then
    assertThat(userTeams).hasSize(0)
  }

  @Test
  fun `매니저가 등록된 후 매니저목록을 입력한다`() {
    // given
    makeManager("user", "team")

    // when
    val userTeams = managerListService.getManagers()

    // then
    assertThat(userTeams).hasSize(1)
    assertThat(userTeams[0].isManager).isTrue
  }
}