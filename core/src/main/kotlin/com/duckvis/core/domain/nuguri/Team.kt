package com.duckvis.core.domain.nuguri

import com.duckvis.core.domain.BaseDuckvisEntity
import javax.persistence.Entity

@Entity
class Team(
  var name: String
) : BaseDuckvisEntity() {

  fun changeName(name: String) {
    this.name = name
  }

}