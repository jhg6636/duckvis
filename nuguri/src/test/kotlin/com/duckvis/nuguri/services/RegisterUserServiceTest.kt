package com.duckvis.nuguri.services

import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.types.shared.UserLevelType
import com.duckvis.nuguri.domain.admin.service.user.RegisterUserService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
class RegisterUserServiceTest(
  @Autowired private val registerUserService: RegisterUserService,
  @Autowired private val userRepository: UserRepository,
) {
  @AfterEach
  @Transactional
  fun clear() {
    userRepository.deleteAllInBatch()
  }

  @Test
  fun `입사한다`() {
    // given

    // when
    val user = registerUserService.saveUser("usercode", "username")

    // then
    assertThat(userRepository.count()).isEqualTo(1)
    assertThat(user.name).isEqualTo("username")
    assertThat(user.level).isEqualTo(UserLevelType.NORMAL)
    assertThat(user.code).isEqualTo("usercode")
  }

  @Test
  fun `퇴사했던 사원이 재입사한다`() {
    // given
    userRepository.save(User("usercode", "username", level = UserLevelType.EXIT))

    // when
    val returnUser = registerUserService.saveUser("usercode", "username")

    // then
    assertThat(userRepository.count()).isEqualTo(1)
    assertThat(returnUser.level).isEqualTo(UserLevelType.NORMAL)
    assertThat(returnUser.name).isEqualTo("username")
  }
}