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
import com.duckvis.nuguri.domain.admin.service.team.DeleteTeamMemberService
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
class DeleteTeamMemberServiceTest(
  @Autowired private val deleteTeamMemberService: DeleteTeamMemberService,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val userTeamRepository: UserTeamRepository,
  @Autowired private val teamRepository: TeamRepository,
) {
  @BeforeEach
  @Transactional
  fun prepare() {
    val user1 = userRepository.save(User("user1", "user1"))
    val user2 = userRepository.save(User("user2", "user2"))
    val manager = userRepository.save(User("manager1", "manager1"))

    val team1 = teamRepository.save(Team("team1"))

    userTeamRepository.save(UserTeam(user1.id, team1.id))
    userTeamRepository.save(UserTeam(manager.id, team1.id, level = UserTeamLevel.MANAGER))
  }

  @AfterEach
  @Transactional
  fun clear() {
    userTeamRepository.deleteAllInBatch()
    userRepository.deleteAllInBatch()
    teamRepository.deleteAllInBatch()
  }

  @Test
  fun `매니저가 한 명인 팀에서 매니저를 팀원삭제`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.NOT_ENOUGH_MANAGER).assert {
      deleteTeamMemberService.deleteTeamMember("manager1", "team1")
    }
  }

  @Test
  fun `팀원을 삭제한다`() {
    // given
    val team = teamRepository.findByName("team1") ?: throw NuguriException(ExceptionType.NO_SUCH_TEAM)
    val user = userRepository.findByName("user1") ?: throw NuguriException(ExceptionType.NO_SUCH_USER)

    // when
    deleteTeamMemberService.deleteTeamMember("user1", "team1")

    // then
    assertThat(userTeamRepository.findAllByTeamId(team.id)).hasSize(1)
    assertThat(userTeamRepository.findByUserIdAndTeamId(user.id, team.id)).isNull()
  }

  @Test
  fun `팀원이 아닌 사람을 삭제한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.NOT_TEAM_MEMBER).assert {
      deleteTeamMemberService.deleteTeamMember("user2", "team1")
    }
  }

  @Test
  fun `모르는 팀에서 팀원을 삭제한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.NO_SUCH_TEAM).assert {
      deleteTeamMemberService.deleteTeamMember("user1", "notTeam")
    }
  }

  @Test
  fun `모르는 사람을 팀에서 삭제한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.NO_SUCH_USER).assert {
      deleteTeamMemberService.deleteTeamMember("notUser", "team1")
    }
  }
}