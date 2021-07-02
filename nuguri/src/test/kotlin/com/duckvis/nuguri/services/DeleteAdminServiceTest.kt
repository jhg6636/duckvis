package com.duckvis.nuguri.services

import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.types.shared.UserLevelType
import com.duckvis.nuguri.domain.admin.service.user.DeleteAdminService
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
class DeleteAdminServiceTest(
  @Autowired private val deleteAdminService: DeleteAdminService,
  @Autowired private val userRepository: UserRepository,
) {
  @BeforeEach
  @Transactional
  fun prepare() {
    userRepository.save(User("user1", "user1"))
    userRepository.save(User("admin1", "admin1", level = UserLevelType.ADMIN))
  }

  @AfterEach
  @Transactional
  fun clear() {
    userRepository.deleteAllInBatch()
  }

  @Test
  fun `등록되어 있는 관리자를 삭제한다`() {
    // given

    // when
    val admin = deleteAdminService.deleteAdmin("admin1")

    // then
    assertThat(admin.isAdmin).isFalse
  }

  @Test
  fun `관리자가 아닌 사람을 관리자삭제 한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.NOT_ADMIN).assert {
      deleteAdminService.deleteAdmin("user1")
    }
  }

  @Test
  fun `사원이 아닌 사람을 관리자삭제 한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.NO_SUCH_USER).assert {
      deleteAdminService.deleteAdmin("notUser")
    }
  }
}