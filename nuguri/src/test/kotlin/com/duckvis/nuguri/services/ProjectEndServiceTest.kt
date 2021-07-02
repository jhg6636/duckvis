package com.duckvis.nuguri.services

import com.duckvis.core.domain.nuguri.Project
import com.duckvis.core.domain.nuguri.ProjectRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.nuguri.domain.admin.service.project.ProjectEndService
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
class ProjectEndServiceTest(
  @Autowired private val projectEndService: ProjectEndService,
  @Autowired private val projectRepository: ProjectRepository,
) {
  @BeforeEach
  @Transactional
  fun prepare() {
    projectRepository.save(Project("기본", "ㄱㅂ"))
  }

  @AfterEach
  @Transactional
  fun clear() {
    projectRepository.deleteAllInBatch()
  }

  @Test
  fun `프로젝트를 종료한다`() {
    // given

    // when
    val project = projectEndService.endProject("기본")

    // then
    assertThat(projectRepository.count()).isEqualTo(1)
    assertThat(project.isFinished).isTrue
  }

  @Test
  fun `없는 프로젝트를 종료한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.NO_SUCH_PROJECT).assert {
      projectEndService.endProject("qwer")
    }
  }
}