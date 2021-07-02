package com.duckvis.nuguri.services

import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.nuguri.domain.admin.service.team.AddTeamService
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
class AddTeamServiceTest(
  @Autowired private val addTeamService: AddTeamService,
  @Autowired private val teamRepository: TeamRepository,
  @Autowired private val userTeamRepository: UserTeamRepository,
  @Autowired private val userRepository: UserRepository,
) {
  lateinit var user1: User
  lateinit var user2: User

  @BeforeEach
  @Transactional
  fun prepare() {
    user1 = userRepository.save(User("user1", "user1"))
    user2 = userRepository.save(User("user2", "user2"))
  }

  @AfterEach
  @Transactional
  fun clear() {
    userTeamRepository.deleteAll()
    teamRepository.deleteAll()
    userRepository.deleteAll()
  }

  @Test
  fun `새 팀을 생성한다`() {
    // given

    // when
    val team = addTeamService.addTeam("team1", "user1")
    val userTeam = userTeamRepository.findByUserIdAndTeamId(user1.id, team.id)

    // then
    assertThat(teamRepository.count()).isEqualTo(1)
    assertThat(userTeam).isNotNull
    assertThat(userTeam!!.isManager).isTrue
  }

  @Test
  fun `이상한 사람을 매니저로 지정한 채 팀을 생성한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.NO_SUCH_USER).assert {
      addTeamService.addTeam("team1", "user55")
    }
  }

  @Test
  fun `이미 있는 팀명으로 팀을 생성한다`() {
    // given
    addTeamService.addTeam("team1", "user1")

    // when & then
    AssertNuguriException(ExceptionType.TEAM_NAME_ALREADY_EXISTS).assert {
      addTeamService.addTeam("team1", "user2")
    }
  }
}