package com.duckvis.nuguri.domain.admin.service.user.v2

import com.duckvis.core.domain.nuguri.UserProfile
import com.duckvis.core.domain.nuguri.UserProfileRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.user.NuguriChangeProfileRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.DiscountOption
import com.duckvis.core.types.nuguri.Gender
import com.duckvis.core.types.nuguri.UserProfileChangeType
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.core.utils.toDurationSeconds
import com.duckvis.core.utils.toKorean
import com.duckvis.nuguri.services.NuguriServiceV2
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.format.DateTimeParseException

@Service("CHANGE_PROFILE_V2")
class ChangeProfileServiceV2(
  private val userProfileRepository: UserProfileRepository,
  private val userRepository: UserRepository,
) : NuguriServiceV2 {

  override val type: CommandMinorType
    get() = CommandMinorType.CHANGE_PROFILE

  @Transactional
  override fun response(serviceRequestDto: NuguriServiceRequestParameterDto): String {
    val params = serviceRequestDto as NuguriChangeProfileRequestParameterDto
    changeProfile(params.userName, params.field, params.content)
    return try {
      "정보가 변경되었어요~\n${params.userName}님의 ${params.field}은(는) 이제 ${
        DateTimeMaker.stringToDate(
          params.content
        )
          .toKorean
      }(이)에요~"
    } catch (e: DateTimeParseException) {
      "정보가 변경되었어요~\n${params.userName}님의 ${params.field}은(는) 이제 ${params.content}(이)에요~"
    }
  }

  fun changeProfile(userName: String, changingField: String, content: String): UserProfile {
    val user = userRepository.findByName(userName) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    val userProfile = userProfileRepository.findByUserId(user.id) ?: throw NuguriException(ExceptionType.NO_SUCH_USER)
    return when (UserProfileChangeType.of(changingField)) {
      UserProfileChangeType.NAME -> {
        user.name = content
        userProfile.apply { changeName(content) }
      }
      UserProfileChangeType.GENDER -> userProfile.apply { changeGender(Gender.of(content)) }
      UserProfileChangeType.BIRTH_DATE -> userProfile.apply { changeBirthDate(DateTimeMaker.stringToDate(content)) }
      UserProfileChangeType.JOIN_DATE -> userProfile.apply { changeJoinDate(DateTimeMaker.stringToDate(content)) }
      UserProfileChangeType.STATUS_MESSAGE -> userProfile.apply { changeStatusMessage(content) }
      UserProfileChangeType.DISCOUNT_OPTION -> userProfile.apply { changeDiscountOption(DiscountOption.of(content)) }
      UserProfileChangeType.TARGET_WORK_HOUR -> userProfile.apply {
        val seconds = content.toDurationSeconds
        changeTargetWorkSeconds(seconds)
      }
    }
  }
}