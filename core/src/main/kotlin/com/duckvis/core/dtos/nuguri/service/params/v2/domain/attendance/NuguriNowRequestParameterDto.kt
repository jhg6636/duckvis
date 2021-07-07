package com.duckvis.core.dtos.nuguri.service.params.v2.domain.attendance

import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto

data class NuguriNowRequestParameterDto(
  override val userCode: String,
  override val userName: String,
  val projectName: String?,
  val subProjectName: String?,
  val teamName: String?,
) : NuguriServiceRequestParameterDto(userCode, userName) {

  companion object {
    fun of(splitText: List<String>, userCode: String, userName: String): NuguriNowRequestParameterDto {
      val projectSubProject = splitText.firstOrNull { text -> text.startsWith("^") && !text.startsWith("^^")}
      val projectName = projectSubProject?.split("_")?.first()?.substring(1)
      val subProjectName = projectSubProject?.split("_")?.getOrNull(1)
      val teamName = splitText.firstOrNull { text -> text.startsWith("^^") }
        ?.substring(1)

      return NuguriNowRequestParameterDto(userCode, userName, projectName, subProjectName, teamName)
    }
  }

}
