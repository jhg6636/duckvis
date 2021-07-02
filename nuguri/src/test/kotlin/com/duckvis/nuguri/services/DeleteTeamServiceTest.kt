package com.duckvis.nuguri.services

import com.duckvis.core.domain.nuguri.Team
import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.domain.nuguri.UserTeam
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.nuguri.domain.admin.service.team.DeleteTeamService
import com.duckvis.nuguri.utils.AssertNuguriException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import javax.transaction.Transactional

@SpringBootTest
@ActiveProfiles("test")
class DeleteTeamServiceTest(
  @Autowired private val deleteTeamService: DeleteTeamService,
  @Autowired private val teamRepository: TeamRepository,
  @Autowired private val userTeamRepository: UserTeamRepository,
) {
  @BeforeEach
  @Transactional
  fun prepare() {
    val team = teamRepository.save(Team("team"))

    userTeamRepository.save(UserTeam(1, team.id))
    userTeamRepository.save(UserTeam(2, team.id))
  }

  @AfterEach
  @Transactional
  fun clear() {
    userTeamRepository.deleteAllInBatch()
    teamRepository.deleteAllInBatch()
  }

  @Test
  fun `팀을 삭제한다`() {
    // given

    // when
    deleteTeamService.deleteTeam("team")

    // then
    assertThat(teamRepository.count()).isEqualTo(0)
    assertThat(userTeamRepository.count()).isEqualTo(0)
  }

  @Test
  fun `모르는 팀을 삭제한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.NO_SUCH_TEAM).assert {
      deleteTeamService.deleteTeam("notTeam")
    }
  }
}