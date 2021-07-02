package com.duckvis.core.domain.shared

import com.duckvis.core.domain.BaseDuckvisEntity
import com.duckvis.core.exceptions.shared.UserAlreadyLivesThereException
import com.duckvis.core.types.shared.CityType
import com.duckvis.core.types.shared.UserLevelType
import com.duckvis.core.types.shared.UserPathType
import org.springframework.context.annotation.ComponentScan
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
@ComponentScan(basePackages = ["com.duckvis"])
class User(
  // slack일 경우 u12fsdf2
  val code: String,
  var name: String,

  @Enumerated(EnumType.STRING)
  var city: CityType = CityType.SEOUL,

  @Enumerated(EnumType.STRING)
  private val path: UserPathType = UserPathType.SLACK,

  @Enumerated(EnumType.STRING)
  var level: UserLevelType = UserLevelType.NORMAL,

  val salaryCode: Long = 0L, // 사원코드 for 너굴
  val fullName: String = "", // 실명 for 너굴

) : BaseDuckvisEntity() {

  val isAdmin: Boolean
    get() = this.level == UserLevelType.ADMIN

  val isGone: Boolean
    get() = this.level == UserLevelType.EXIT

  val isNormal: Boolean
    get() = this.level == UserLevelType.NORMAL

  val tagString: String
    get() = "<@${this.code}>"

  val isBot: Boolean
    get() = code == "USLACKBOT" || code == "U01QM07J8NP"

  fun changeCity(city: CityType) {
    if (this.city == city) throw UserAlreadyLivesThereException()
    this.city = city
  }

  fun upgradeUserLevel() {
    this.level = UserLevelType.ADMIN
  }

  fun downgradeUserLevel() {
    this.level = UserLevelType.NORMAL
  }

  fun exit() {
    this.level = UserLevelType.EXIT
  }

  fun rejoin() {
    this.level = UserLevelType.NORMAL
  }
}