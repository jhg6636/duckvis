package com.duckvis.nuguri.domain.admin.service.user

import com.duckvis.core.domain.nuguri.UserProfile
import com.duckvis.core.domain.nuguri.UserProfileRepository
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.DiscountOption
import com.duckvis.core.types.nuguri.Gender
import com.duckvis.core.types.nuguri.UserProfileChangeType
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.core.utils.toDurationSeconds
import com.duckvis.core.utils.toKorean
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.format.DateTimeParseException

/**
 * 정보변경 기능
 */
@Service("CHANGE_PROFILE")
class ChangeProfileService(
  @Autowired private val userProfileRepository: UserProfileRepository,
  @Autowired private val userRepository: UserRepository,
) : NuguriService {

  override val minimumRequestParams: Int
    get() = 2

  override val maximumRequestParams: Int
    get() = 2

  override val minimumPermission: ServicePermission
    get() = ServicePermission.MEMBER

  @Transactional
  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    changeProfile(serviceRequestDto.userName, serviceRequestDto.params[0], serviceRequestDto.params[1])
    return try {
      "정보가 변경되었어요~\n${serviceRequestDto.userName}님의 ${serviceRequestDto.params[0]}은(는) 이제 ${
        DateTimeMaker.stringToDate(
          serviceRequestDto.params[1]
        )
          .toKorean
      }(이)에요~"
    } catch (e: DateTimeParseException) {
      "정보가 변경되었어요~\n${serviceRequestDto.userName}님의 ${serviceRequestDto.params[0]}은(는) 이제 ${serviceRequestDto.params[1]}(이)에요~"
    }
  }

  @Transactional
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