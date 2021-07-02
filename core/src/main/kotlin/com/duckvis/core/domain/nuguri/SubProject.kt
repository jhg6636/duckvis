package com.duckvis.core.domain.nuguri

import com.duckvis.core.domain.BaseDuckvisEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(
  uniqueConstraints = [UniqueConstraint(
    name = "uni_sub_project_1",
    columnNames = ["project_id", "name"]
  ), UniqueConstraint(
    name = "uni_sub_project_2",
    columnNames = ["project_id", "nickname"]
  )]
)
class SubProject(
  @Column(name = "project_id")
  private val projectId: Long,

  val name: String,
  val nickname: String,
  var isFinished: Boolean = false,
) : BaseDuckvisEntity() {

  val fullName: String
    get() = "[$name($nickname)]"

  fun deleteSubProject() {
    this.isFinished = true
  }

}