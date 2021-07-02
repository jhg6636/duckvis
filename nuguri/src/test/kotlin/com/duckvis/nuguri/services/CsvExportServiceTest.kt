package com.duckvis.nuguri.services

import com.duckvis.core.domain.nuguri.*
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.Work
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.UserTeamLevel
import com.duckvis.nuguri.domain.statistics.service.CsvExportService
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
class CsvExportServiceTest(
  @Autowired private val csvExportService: CsvExportService,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val projectRepository: ProjectRepository,
  @Autowired private val attendanceCardRepository: AttendanceCardRepository,
  @Autowired private val userTeamRepository: UserTeamRepository,
  @Autowired private val teamRepository: TeamRepository,
  @Autowired private val userProfileRepository: UserProfileRepository,
) {
  lateinit var project: Project

  @BeforeEach
  @Transactional
  fun prepare() {
    val user1 = userRepository.save(User("user1", "user1"))
    val user2 = userRepository.save(User("user2", "user2"))
    val user3 = userRepository.save(User("user3", "user3"))
    val team = teamRepository.save(Team("팀"))
    userTeamRepository.save(UserTeam(user1.id, team.id))
    userTeamRepository.save(UserTeam(user2.id, team.id, UserTeamLevel.MANAGER))
    userTeamRepository.save(UserTeam(user3.id, team.id))
    userProfileRepository.save(UserProfile(user1.id, "user1"))
    userProfileRepository.save(UserProfile(user2.id, "user2"))
    userProfileRepository.save(UserProfile(user3.id, "user3"))

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
    userTeamRepository.deleteAllInBatch()
    userProfileRepository.deleteAllInBatch()
    userRepository.deleteAllInBatch()
    teamRepository.deleteAllInBatch()
  }

  @Test
  fun `모든 사람의 출근 기록이 5회인 상황에서 익스포트한다`() {
//    // given
//    attendBetween("user1", DateTimeMaker.nowDateTime().minusHours(6), DateTimeMaker.nowDateTime())
//    attendBetween("user2", DateTimeMaker.nowDateTime().minusHours(3), DateTimeMaker.nowDateTime().minusMinutes(20))
//    attendBetween("user2", DateTimeMaker.nowDateTime().minusHours(6), DateTimeMaker.nowDateTime().minusHours(4))
//    attendBetween("user3", DateTimeMaker.nowDateTime().minusHours(14), DateTimeMaker.nowDateTime().minusHours(12))
//    attendBetween("user3", DateTimeMaker.nowDateTime().minusHours(1), DateTimeMaker.nowDateTime())
//
//    // when
//    val string = csvExportService.salaryExportCsv(DateTimeMaker.nowDate().monthValue)
//
//    println("csvString: $string")
//
//    // then
//    assertThat(string.split("\n")).hasSize(10)
  }
}