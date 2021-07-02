package com.duckvis.scheduler.nuguri

import com.duckvis.core.domain.nuguri.*
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.Work
import com.duckvis.core.dtos.nuguri.WorkTypeDto
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.nuguri.domain.admin.service.user.ChangeProfileService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
internal class EveryFirstDaySchedulerTest(
  @Autowired private val everyFirstDayScheduler: EveryFirstDayScheduler,
  @Autowired private val holidayRepository: HolidayRepository,
  @Autowired private val userProfileRepository: UserProfileRepository,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val attendanceCardRepository: AttendanceCardRepository,
  @Autowired private val projectRepository: ProjectRepository,
  @Autowired private val changeProfileService: ChangeProfileService,
) {

  @BeforeEach
  fun prepare() {
    projectRepository.save(Project("월말수정", "월말수정"))
  }

  @AfterEach
  fun clear() {
    holidayRepository.deleteAllInBatch()
    userProfileRepository.deleteAllInBatch()
    userRepository.deleteAllInBatch()
    attendanceCardRepository.deleteAllInBatch()
    projectRepository.deleteAllInBatch()
  }

  @Test
  fun `양력 법정공휴일이 모두 잘 등록된다`() {
    // given

    // when
    everyFirstDayScheduler.saveAllNationalHolidays()

    // then
    assertThat(holidayRepository.count()).isEqualTo(8)
  }

  @Disabled
  @Test
  fun `근무목표가 자동설정되고 휴가와 병가가 리셋된다`() {
    // given
    val user = userRepository.save(User("", "이름"))
    val userProfile = userProfileRepository.save(UserProfile(user.id, "이름"))

    userProfile.changeTargetWorkSeconds(12345)
    userProfile.changeDayOff(2)
    userProfile.changeDayOffSick(3)

    // when
    everyFirstDayScheduler.resetUserProfiles()

    // then
    val changedUserProfile = userProfileRepository.findByUserId(user.id)!!
    assertThat(changedUserProfile.dayOff).isEqualTo(0)
    assertThat(changedUserProfile.dayOffSick).isEqualTo(0)
    assertThat(changedUserProfile.targetWorkSeconds).isEqualTo(637714) // 5월 기준
  }

  @Test
  fun `주말이 제대로 설정된다`() {
    // given

    // when
    everyFirstDayScheduler.setWeekends()

    // then
    assertThat(holidayRepository.count()).isEqualTo(8) // 6월 기준
  }

  @Test
  fun `기본 근무 다 안찍은 사람이 연장을 찍으면?`() {
    // given
    val user = userRepository.save(User("user", "이름"))
    val userProfile = userProfileRepository.save(UserProfile(user.id, "이름"))
    changeProfileService.changeProfile("이름", "근무시간", "0:20")

    saveCards(user.id)

    // when
    everyFirstDayScheduler.modifyExtendFaults()

    // then
    val extendedTime = attendanceCardRepository.findAll().filter { it.isExtended }.sumBy { it.durationSeconds ?: 0 }
    val normalTime =
      attendanceCardRepository.findAll().filter { it.isSameWorkType(WorkTypeDto()) }.sumBy { it.durationSeconds ?: 0 }
    assertThat(attendanceCardRepository.count()).isEqualTo(4)
    assertThat(normalTime).isEqualTo(1200)
    assertThat(extendedTime).isEqualTo(100)
  }

  @Transactional
  fun saveCards(userId: Long) {

    attendanceCardRepository.saveAll(
      listOf(
        AttendanceCard(
          Work(userId, 0L, null),
          DateTimeMaker.nowDateTime().minusMonths(1),
          1100,
          DateTimeMaker.nowDateTime().minusMonths(1)
        ),
        AttendanceCard(
          Work(userId, 0L, null, WorkTypeDto(isExtended = true)),
          DateTimeMaker.nowDateTime().minusMonths(1),
          200,
          DateTimeMaker.nowDateTime().minusMonths(1)
        )
      )
    )
  }

}