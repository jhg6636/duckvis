package com.duckvis.nuguri.services

import com.duckvis.core.domain.nuguri.*
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.Work
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.nuguri.domain.attendance.service.NowService
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
class NowServiceTest(
  @Autowired private val nowService: NowService,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val projectRepository: ProjectRepository,
  @Autowired private val attendanceCardRepository: AttendanceCardRepository,
  @Autowired private val subProjectRepository: SubProjectRepository,
) {
  @BeforeEach
  @Transactional
  fun prepare() {
    userRepository.save(User("user1", "user1"))
    userRepository.save(User("user2", "user2"))
    userRepository.save(User("user3", "user3"))
    userRepository.save(User("user4", "user4"))
    userRepository.save(User("user5", "user5"))

    val project = projectRepository.save(Project("기본", "ㄱㅂ"))
    projectRepository.save(Project("회의", "ㅎㅇ"))

    subProjectRepository.save(SubProject(project.id, "섭1", "ㅅ1"))
  }

  @AfterEach
  @Transactional
  fun clear() {
    attendanceCardRepository.deleteAllInBatch()
    projectRepository.deleteAllInBatch()
    userRepository.deleteAllInBatch()
  }

  @Transactional
  fun attendBeforeHours(userName: String, hours: Long, projectName: String = "기본") {
    val user = userRepository.findByName(userName) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val project = projectRepository.findByNameOrNickname(projectName, projectName) ?: throw NuguriException(
      ExceptionType.NO_SUCH_PROJECT
    )

    val work = Work(user.id, project.id, null)
    attendanceCardRepository.save(AttendanceCard(work, DateTimeMaker.nowDateTime().minusHours(hours)))
  }

  @Test
  fun `한 명도 출근하지 않은 상황에서 지금을 입력한다`() {
    // given

    // when
    val nowList = nowService.now(null, null, null)

    // then
    assertThat(nowList).isEmpty()
  }

  @Test
  fun `출근한 사람이 있는 상황에서 지금을 입력한다`() {
    // given
    attendBeforeHours("user1", 2)
    attendBeforeHours("user2", 3)
    attendBeforeHours("user3", 4)

    // when
    val nowList = nowService.now(null, null, null)
    println(nowList.joinToString("\n"))

    // then
    assertThat(nowList).hasSize(3)
  }

  @Test
  fun `해당 프로젝트에 출근한 사람을 확인하기 위해 지금을 입력한다`() {
    // given
    attendBeforeHours("user1", 2)
    attendBeforeHours("user2", 3, "회의")
    attendBeforeHours("user3", 4)

    // when
    val nowList = nowService.now("회의", null, null)

    // then
    assertThat(nowList).hasSize(1)
    assertThat(nowList[0].projectName).isEqualTo("회의")
    assertThat(nowList[0].userName).isEqualTo("user2")
  }
}