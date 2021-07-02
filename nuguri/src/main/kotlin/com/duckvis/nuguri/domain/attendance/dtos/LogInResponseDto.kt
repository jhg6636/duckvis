package com.duckvis.nuguri.domain.attendance.dtos

data class LogInResponseDto(
  val userName: String,
  val projectName: String,
  val beforeProject: String?,
  private val subProjectName: String?,
) {

  val responseString: String
    get() {
      val logOutMessage = if (this.beforeProject != null) {
        "앗! ${this.userName}님은 이미 ${this.beforeProject}에서 근무중이셨어요!" +
          "그럼 이건 퇴근처리해둘게요~\n뭐 아무튼 "
      } else {
        ""
      }

      val projectDescription = if (subProjectName == null) {
        "${this.projectName} 프로젝트"
      } else {
        "${this.projectName} 프로젝트 (${this.subProjectName} 서브프로젝트)"
      }

      return "${logOutMessage}${this.userName}님! ${projectDescription}로 출근하셨네요!\n" +
        "공격적인 근무 기대할게요~"
    }

}