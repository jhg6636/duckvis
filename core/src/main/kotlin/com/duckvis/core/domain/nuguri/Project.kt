package com.duckvis.core.domain.nuguri

import com.duckvis.core.domain.BaseDuckvisEntity
import javax.persistence.Entity

@Entity
class Project(
  val name: String,
  val nickname: String,
  var isFinished: Boolean = false
) : BaseDuckvisEntity() {

  val fullName: String
    get() = "$name($nickname)"

  fun deleteProject() {
    this.isFinished = true
  }

}