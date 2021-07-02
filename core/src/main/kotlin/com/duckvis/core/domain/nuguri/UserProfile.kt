package com.duckvis.core.domain.nuguri

import com.duckvis.core.domain.BaseDuckvisEntity
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.DiscountOption
import com.duckvis.core.types.nuguri.Gender
import com.duckvis.core.utils.secondsToString
import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

// THINKING 굳이 분리를 했어야 했나
@Entity
class UserProfile(
  val userId: Long,
  var name: String,
) : BaseDuckvisEntity() {

  @Enumerated(EnumType.STRING)
  var gender: Gender = Gender.UNDEFINED

  var birthDate: LocalDate = LocalDate.of(2000, 1, 1)
  var joinDate: LocalDate = LocalDate.of(2000, 1, 1)
  var statusMessage: String = "입력되지 않음"

  //var targetWorkHour: Int = 0
  var targetWorkSeconds: Int = 0

  @Enumerated(EnumType.STRING)
  var discountOption: DiscountOption = DiscountOption.VACATION
  var dayOff: Int = 0
  var dayOffSick: Int = 0

  val detailProfileString: String
    get() = "$profileString\n:calendar:근무정책(이번달 근무시간이 부족하면 어디서 차감?): ${discountOption}에서\n" +
      ":clock9:근무목표: ${targetWorkSeconds.secondsToString}"

  val profileString: String
    get() = "${name}님의 정보에요~\n:bust_in_silhouette:이름: ${name}\n:restroom:성별: ${gender}\n" +
      ":speech_balloon:상태메시지: ${statusMessage}\n" +
      ":birthday:생일: ${birthDate.monthValue}월 ${birthDate.dayOfMonth}일\n" +
      ":computer:입사일: ${joinDate.monthValue}월 ${joinDate.dayOfMonth}일"

  val birthdayString: String
    get() = "앗! 오늘 ${name}님의 생일이네요~?\n생일 축하합니다!!!!\n"

  val realTargetSeconds: Int
    get() = targetWorkSeconds - (dayOff + dayOffSick) * 8 * 60 * 60

  val isUsingDayOff: Boolean
    get() = this.dayOff > 0 || this.dayOffSick > 0

  fun changeName(newName: String) {
    this.name = newName
  }

  fun changeStatusMessage(message: String) {
    this.statusMessage = message
  }

  fun changeGender(gender: Gender) {
    this.gender = gender
  }

  fun changeBirthDate(birthDate: LocalDate) {
    this.birthDate = birthDate
  }

  fun changeJoinDate(joinDate: LocalDate) {
    this.joinDate = joinDate
  }

  fun changeTargetWorkSeconds(target: Int) {
    this.targetWorkSeconds = target
  }

  fun changeDiscountOption(discountOption: DiscountOption) {
    this.discountOption = discountOption
  }

  fun changeDayOff(days: Int) {
    this.dayOff = days
  }

  fun changeDayOffSick(days: Int) {
    this.dayOffSick = days
  }

  fun minusTargetSeconds(amount: Int) {
    this.targetWorkSeconds -= amount
  }

  fun isBirthday(date: LocalDate): Boolean {
    return this.birthDate.monthValue == date.monthValue && this.birthDate.dayOfMonth == date.dayOfMonth
  }

}