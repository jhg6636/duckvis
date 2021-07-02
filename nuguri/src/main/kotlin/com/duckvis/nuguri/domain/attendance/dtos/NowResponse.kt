package com.duckvis.nuguri.domain.attendance.dtos

data class NowResponse(
  val userName: String,
  val projectName: String,
  val subProjectName: String?,
) {

  val nowString: String
    get() = if (subProjectName == null) {
      ":pushpin:${this.userName}님은 ${this.projectName} 프로젝트에서"
    } else {
      ":pushpin:${this.userName}님은 ${this.projectName} 프로젝트 (${this.subProjectName} 서브프로젝트)에서"
    }

}