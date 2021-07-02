package com.duckvis.nuguri.services

import com.duckvis.core.domain.nuguri.UserProfile
import com.duckvis.core.domain.nuguri.UserProfileRepository
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.nuguri.domain.admin.service.user.SetDayOffService
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
class SetDayOffServiceTest(
  @Autowired private val setDayOffService: SetDayOffService,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val userProfileRepository: UserProfileRepository,
) {
  @BeforeEach
  @Transactional
  fun prepare() {
    val user = userRepository.save(User("user", "user"))
    userProfileRepository.save(UserProfile(user.id, "user"))
  }

  @AfterEach
  @Transactional
  fun clear() {
    userProfileRepository.deleteAllInBatch()
    userRepository.deleteAllInBatch()
  }

  @Test
  fun `휴가를 이틀 사용한다`() {
    // given

    // when
    val profile = setDayOffService.setDayOff("user", 2)

    // then
    assertThat(profile).isEqualTo(2)
  }

  @Test
  fun `휴가 사용을 취소한다`() {
    // given
    setDayOffService.setDayOff("user", 2)

    // when
    val profile = setDayOffService.setDayOff("user", -2)

    // then
    assertThat(profile).isEqualTo(0)
  }
}