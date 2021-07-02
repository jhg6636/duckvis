package com.duckvis.nuguri.services

import com.duckvis.core.domain.nuguri.Team
import com.duckvis.core.domain.nuguri.TeamRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.nuguri.domain.admin.service.team.ChangeTeamNameService
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
class ChangeTeamNameServiceTest(
  @Autowired private val teamRepository: TeamRepository,
  @Autowired private val changeTeamNameService: ChangeTeamNameService,
) {
  lateinit var team1: Team
  lateinit var team2: Team

  @BeforeEach
  @Transactional
  fun prepare() {
    team1 = teamRepository.save(Team("team1"))
    team2 = teamRepository.save(Team("team2"))
  }

  @AfterEach
  @Transactional
  fun clear() {
    teamRepository.deleteAll()
  }

  @Test
  @Transactional
  fun `팀 이름을 변경한다`() {
    // given

    // when
    changeTeamNameService.changeTeamName("team1", "newTeam")

    // then
    assertThat(team1.name).isEqualTo("newTeam")
  }


  @Test
  fun `현재의 팀 이름으로 팀 이름을 변경한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.CANNOT_CHANGE).assert {
      changeTeamNameService.changeTeamName("team2", "team2")
    }
  }

  @Test
  fun `이미 존재하는 팀 이름으로 팀 이름을 변경한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.TEAM_NAME_ALREADY_EXISTS).assert {
      changeTeamNameService.changeTeamName("team1", "team2")
    }
  }
}