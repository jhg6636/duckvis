package com.duckvis.scheduler.nuguri

import com.duckvis.core.domain.nuguri.AttendanceCardRepository
import com.duckvis.core.domain.shared.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CoreTimeEndSchedulerTest @Autowired constructor(
  private val coreTimeEndScheduler: CoreTimeEndScheduler,
  private val attendanceCardRepository: AttendanceCardRepository,
  private val userRepository: UserRepository,
) {

  fun `코어타임 근무 미달멤버 띄워준다(getNotEnoughWorkedUsers Test)`() {
    // given

    // when

    // then
  }

}