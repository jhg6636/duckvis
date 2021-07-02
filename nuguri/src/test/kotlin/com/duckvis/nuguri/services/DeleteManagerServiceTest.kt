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
import com.duckvis.core.types.shared.UserLevelType
import com.duckvis.nuguri.domain.admin.service.team.DeleteManagerService
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
class DeleteManagerServiceTest(
  @Autowired private val deleteManagerService: DeleteManagerService,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val userTeamRepository: UserTeamRepository,
  @Autowired private val teamRepository: TeamRepository,
) {
  @BeforeEach
  @Transactional
  fun prepare() {
    val user1 = userRepository.save(User("user1", "user1"))
    val user2 = userRepository.save(User("user2", "user2"))
    val manager1 = userRepository.save(User("manager1", "manager1"))
    val manager2 = userRepository.save(User("manager2", "manager2", level = UserLevelType.ADMIN))
    val manager3 = userRepository.save(User("manager3", "manager3"))

    val team1 = teamRepository.save(Team("team1"))
    val team2 = teamRepository.save(Team("team2"))

    userTeamRepository.save(UserTeam(user1.id, team1.id))
    userTeamRepository.save(UserTeam(user2.id, team2.id))
    userTeamRepository.save(UserTeam(manager1.id, team1.id, UserTeamLevel.MANAGER))
    userTeamRepository.save(UserTeam(manager2.id, team1.id, UserTeamLevel.MANAGER))
    userTeamRepository.save(UserTeam(manager3.id, team2.id, UserTeamLevel.MANAGER))
  }

  @AfterEach
  fun clear() {
    userTeamRepository.deleteAllInBatch()
    userRepository.deleteAllInBatch()
    teamRepository.deleteAllInBatch()
  }

  @Test
  fun `매니저가 2명인 팀의 매니저를 삭제한다`() {
    // given

    // when
    val userTeam = deleteManagerService.deleteManager("team1", "manager1")

    // then
    assertThat(userTeam.isManager).isFalse
  }

  @Test
  fun `매니저가 1명인 팀의 매니저를 삭제한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.NOT_ENOUGH_MANAGER).assert {
      deleteManagerService.deleteManager("team2", "manager3")
    }
  }

  @Test
  @Transactional
  fun `현재 관리자인 매니저를 매니저삭제 한다`() {
    // given
    val user = userRepository.findByName("manager2") ?: throw NuguriException(ExceptionType.NO_SUCH_USER)

    // when
    val userTeam = deleteManagerService.deleteManager("team1", "manager2")

    // then
    assertThat(user.isAdmin).isTrue
    assertThat(userTeam.isManager).isFalse
  }

  @Test
  fun `매니저가 아닌 팀원을 매니저삭제한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.NOT_MANAGER).assert {
      deleteManagerService.deleteManager("team1", "user1")
    }
  }

  @Test
  fun `다른 팀의 매니저를 매니저삭제한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.NOT_TEAM_MEMBER).assert {
      deleteManagerService.deleteManager("team1", "manager3")
    }
  }
}