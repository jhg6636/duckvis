package com.duckvis.nuguri.services

import com.duckvis.core.domain.nuguri.Project
import com.duckvis.core.domain.nuguri.ProjectRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.nuguri.domain.admin.service.project.ProjectStartService
import com.duckvis.nuguri.utils.AssertNuguriException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
class ProjectStartServiceTest(
  @Autowired private val projectStartService: ProjectStartService,
  @Autowired private val projectRepository: ProjectRepository,
) {
  @AfterEach
  @Transactional
  fun clear() {
    projectRepository.deleteAllInBatch()
  }

  @Transactional
  fun addProject(name: String, nickname: String) {
    projectRepository.save(Project(name, nickname))
  }

  @Test
  fun `플젝을 시작한다`() {
    // given

    // when
    projectStartService.startProject("기본", "ㄱㅂ")

    // then
    assertThat(projectRepository.count()).isEqualTo(1)
  }

  @Test
  fun `이미 있는 이름의 플젝을 시작한다`() {
    // given
    projectStartService.startProject("기본", "ㄱㅂ")

    // when & then
    AssertNuguriException(ExceptionType.PROJECT_NAME_ALREADY_EXISTS).assert {
      projectStartService.startProject("기본", "ㄱㄱㅂㅂ")
    }
  }

  @Test
  fun `이미 있는 별칭의 플젝을 시작한다`() {
    // given
    projectStartService.startProject("기본", "ㄱㅂ")

    // when & then
    AssertNuguriException(ExceptionType.PROJECT_NICKNAME_ALREADY_EXISTS).assert {
      projectStartService.startProject("기기본본", "ㄱㅂ")
    }
  }

  @Test
  fun `없는 이름과 별칭으로 플젝을 시작한다`() {
    // given
    projectStartService.startProject("기기본본", "ㄲㅃ")

    // when
    projectStartService.startProject("기본", "ㄱㅂ")

    // then
    assertThat(projectRepository.count()).isEqualTo(2)
  }
}