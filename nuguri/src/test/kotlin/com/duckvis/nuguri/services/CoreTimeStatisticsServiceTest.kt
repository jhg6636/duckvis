package com.duckvis.nuguri.services

import com.duckvis.core.domain.nuguri.AttendanceCard
import com.duckvis.core.domain.nuguri.AttendanceCardRepository
import com.duckvis.core.domain.nuguri.Project
import com.duckvis.core.domain.nuguri.ProjectRepository
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.Work
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.core.utils.StartAndEndTime
import com.duckvis.nuguri.domain.statistics.service.CoreTimeStatisticsService
import com.duckvis.nuguri.utils.AssertNuguriException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDateTime

@SpringBootTest
@ActiveProfiles("test")
class CoreTimeStatisticsServiceTest(
  @Autowired private val coreTimeStatisticsService: CoreTimeStatisticsService,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val projectRepository: ProjectRepository,
  @Autowired private val attendanceCardRepository: AttendanceCardRepository,
) {
  lateinit var project: Project

  @BeforeEach
  @Transactional
  fun prepare() {
    userRepository.save(User("user1", "user1"))
    userRepository.save(User("user2", "user2"))
    userRepository.save(User("user3", "user3"))

    project = projectRepository.save(Project("기본", "ㄱㅂ"))
  }

  @Transactional
  fun attendBetween(userName: String, startTime: LocalDateTime, endTime: LocalDateTime): AttendanceCard {
    val user = userRepository.findByName(userName) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val work = Work(user.id, project.id, null)
    val card = AttendanceCard(work, startTime, Duration.between(startTime, endTime).toSeconds().toInt(), endTime)

    return attendanceCardRepository.save(card)
  }

  @AfterEach
  @Transactional
  fun clear() {
    attendanceCardRepository.deleteAllInBatch()
    projectRepository.deleteAllInBatch()
    userRepository.deleteAllInBatch()
  }

  @Test
  fun `코어타임 통계를 입력한다`() {
    // given
    attendBetween(
      "user1",
      StartAndEndTime.coreTimeStart,
      StartAndEndTime.coreTimeEnd
    )
    attendBetween(
      "user2",
      StartAndEndTime.coreTimeStart,
      StartAndEndTime.coreTimeStart.plusHours(2)
    )
    attendBetween(
      "user3",
      StartAndEndTime.coreTimeEnd.minusHours(3).minusMinutes(30),
      StartAndEndTime.coreTimeEnd
    )

    // when
    val memberDurations = coreTimeStatisticsService.coreTimeStatistics(DateTimeMaker.nowDate())
    println(memberDurations)

    // then
  }

  @Test
  fun `아무도 일하지 않았을 때 코어타임 통계를 입력한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.NOBODY_HAS_WORKED).assert {
      coreTimeStatisticsService.coreTimeStatistics(DateTimeMaker.nowDate())
    }
  }
}