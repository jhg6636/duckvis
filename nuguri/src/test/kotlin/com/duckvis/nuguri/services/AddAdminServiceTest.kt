package com.duckvis.nuguri.services

import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.nuguri.domain.admin.service.user.AddAdminService
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
class AddAdminServiceTest(
  @Autowired private val addAdminService: AddAdminService,
  @Autowired private val userRepository: UserRepository,
) {
  @BeforeEach
  @Transactional
  fun prepare() {
    userRepository.save(User("user1", "user1"))
  }

  @AfterEach
  @Transactional
  fun clear() {
    userRepository.deleteAll()
  }

  @Test
  fun `사원 중 한 명을 관리자로 지정한다`() {
    // given

    // when
    addAdminService.addAdmin("user1")
    val user1 = userRepository.findByName("user1")

    // then
    assertThat(user1?.isAdmin).isTrue
  }

  @Test
  fun `사원이 아닌 사람을 관리자로 지정한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.NO_SUCH_USER).assert {
      addAdminService.addAdmin("user2")
    }
  }

  @Test
  fun `이미 관리자인 사람을 관리자로 지정한다`() {
    // given
    addAdminService.addAdmin("user1")

    // when & then
    AssertNuguriException(ExceptionType.ALREADY_ADMIN).assert {
      addAdminService.addAdmin("user1")
    }
  }
}