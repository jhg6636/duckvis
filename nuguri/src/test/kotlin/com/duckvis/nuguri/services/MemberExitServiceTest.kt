package com.duckvis.nuguri.services

import com.duckvis.core.domain.nuguri.UserTeam
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.types.nuguri.UserTeamLevel
import com.duckvis.nuguri.domain.admin.service.user.MemberExitService
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
class MemberExitServiceTest(
  @Autowired private val memberExitService: MemberExitService,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val userTeamRepository: UserTeamRepository,
) {
  @BeforeEach
  @Transactional
  fun prepare() {
    val user1 = userRepository.save(User("user1", "user1"))
    val user2 = userRepository.save(User("user2", "user2"))
    val user3 = userRepository.save(User("user3", "user3"))
    val user4 = userRepository.save(User("user4", "user4"))
    val user5 = userRepository.save(User("user5", "user5"))
    val manager1 = userRepository.save(User("manager1", "manager1"))
    val manager2 = userRepository.save(User("manager2", "manager2"))
    val manager3 = userRepository.save(User("manager3", "manager3"))

    userTeamRepository.save(UserTeam(user1.id, 1))
    userTeamRepository.save(UserTeam(user2.id, 1))
    userTeamRepository.save(UserTeam(user3.id, 2))
    userTeamRepository.save(UserTeam(user4.id, 2))
    userTeamRepository.save(UserTeam(user5.id, 2))

    userTeamRepository.save(UserTeam(manager1.id, 1, UserTeamLevel.MANAGER))
    userTeamRepository.save(UserTeam(manager2.id, 2, UserTeamLevel.MANAGER))
    userTeamRepository.save(UserTeam(manager3.id, 2, UserTeamLevel.MANAGER))
  }

  @AfterEach
  @Transactional
  fun clear() {
    userTeamRepository.deleteAllInBatch()
    userRepository.deleteAllInBatch()
  }

  @Test
  fun `퇴사한다`() {
    // given

    // when
    val exitUser = memberExitService.exit("user1")

    // then
    assertThat(exitUser.isGone).isTrue
    assertThat(userRepository.count()).isEqualTo(8)
  }

  @Test
  fun `팀의 유일한 매니저가 퇴사한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.NOT_ENOUGH_MANAGER).assert {
      memberExitService.exit("manager1")
    }
  }

  @Test
  fun `모르는 사람이 퇴사한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.NO_SUCH_USER).assert {
      memberExitService.exit("notUser")
    }
  }
}