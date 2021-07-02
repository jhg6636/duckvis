package com.duckvis.nuguri.services

import com.duckvis.core.domain.nuguri.UserProfile
import com.duckvis.core.domain.nuguri.UserProfileRepository
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.types.nuguri.DiscountOption
import com.duckvis.core.types.nuguri.Gender
import com.duckvis.nuguri.domain.admin.service.user.ChangeProfileService
import com.duckvis.nuguri.utils.AssertNuguriException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.format.DateTimeParseException

@SpringBootTest
@ActiveProfiles("test")
class ChangeProfileServiceTest(
  @Autowired private val changeProfileService: ChangeProfileService,
  @Autowired private val userRepository: UserRepository,
  @Autowired private val userProfileRepository: UserProfileRepository,
) {
  lateinit var user: User
  lateinit var profile: UserProfile

  @BeforeEach
  @Transactional
  fun prepare() {
    user = userRepository.save(User("user", "user"))
    profile = userProfileRepository.save(UserProfile(user.id, "user"))
  }

  @AfterEach
  @Transactional
  fun clear() {
    userProfileRepository.deleteAll()
    userRepository.deleteAll()
  }

  @Test
  fun `성별을 바꾼다`() {
    // given

    // when
    val changed = changeProfileService.changeProfile("user", "성별", "남자")

    // then
    assertThat(changed.gender).isEqualTo(Gender.MALE)
  }

  @Test
  fun `생일을 바꾼다`() {
    // given

    // when
    val changed = changeProfileService.changeProfile("user", "생일", "0803")

    // then
    assertThat(changed.birthDate.monthValue).isEqualTo(8)
    assertThat(changed.birthDate.dayOfMonth).isEqualTo(3)
  }

  @Test
  fun `입사일을 바꾼다`() {
    // given

    // when
    val changed = changeProfileService.changeProfile("user", "입사일", "0701")

    // then
    assertThat(changed.joinDate.monthValue).isEqualTo(7)
    assertThat(changed.joinDate.dayOfMonth).isEqualTo(1)
  }

  @Test
  fun `상메를 바꾼다`() {
    // given

    // when
    val changed = changeProfileService.changeProfile("user", "상메", "ㅎㅇㅎㅇ")

    // then
    assertThat(changed.statusMessage).isEqualTo("ㅎㅇㅎㅇ")
  }

  @Test
  fun `근무시간이 부족하면을 바꾼다`() {
    // given

    // when
    val changed = changeProfileService.changeProfile("user", "부족하면", "월급에서")

    // then
    assertThat(changed.discountOption).isEqualTo(DiscountOption.PAY)
  }

  @Test
  fun `근무목표를 바꾼다`() {
    // given

    // when
    val changed = changeProfileService.changeProfile("user", "근무시간", "77")

    // then
    assertThat(changed.targetWorkSeconds).isEqualTo(77 * 3600)
  }

  @Test
  @Transactional
  fun `이름을 바꾼다`() {
    // given

    // when
    val changed = changeProfileService.changeProfile("user", "이름개명", "useruser")

    // then
    assertThat(changed.name).isEqualTo("useruser")
    assertThat(user.name).isEqualTo("useruser")
  }

  @Test
  fun `없는 필드를 바꾸려 한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.USER_PROFILE_CHANGE_TYPE_TYPO).assert {
      changeProfileService.changeProfile("user", "월급", "100000000000")
    }
  }

  @Test
  fun `날짜를 이상하게 입력한다`() {
    // given

    // when & then
    assertThrows<DateTimeParseException> {
      changeProfileService.changeProfile("user", "입사일", "0070")
    }
  }

  @Test
  fun `부족하면 옵션을 휴가에서, 월급에서 말고 다른 것을 입력한다`() {
    // given

    // when & then
    AssertNuguriException(ExceptionType.DISCOUNT_OPTION_TYPO).assert {
      changeProfileService.changeProfile("user", "부족하면", "양말에서")
    }
  }

}