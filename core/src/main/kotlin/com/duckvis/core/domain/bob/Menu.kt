package com.duckvis.core.domain.bob

import com.duckvis.core.domain.BaseDuckvisEntity
import javax.persistence.Entity

@Entity
class Menu(
  val name: String
) : BaseDuckvisEntity() {
  fun equals(other: Menu): Boolean {
    return this.name == other.name
  }
}