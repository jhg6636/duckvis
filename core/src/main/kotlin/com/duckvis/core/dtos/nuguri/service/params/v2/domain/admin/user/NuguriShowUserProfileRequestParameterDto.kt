package com.duckvis.core.dtos.nuguri.service.params.v2.domain.admin.user

import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException

data class NuguriShowUserProfileRequestParameterDto(
  override val userCode: String,
  override val userName: String,
  val memberName: String,
  val isDetail: Boolean,
) : NuguriServiceRequestParameterDto(userCode, userName) {

  companion object {
    fun of(splitText: List<String>, userCode: String, userName: String): NuguriShowUserProfileRequestParameterDto {
      val memberName = when (splitText.size) {
        1 -> userName
        2 -> splitText[1]
        else -> throw NuguriException(ExceptionType.TYPO)
      }
      val isDetail = splitText.first().contains("세부")

      return NuguriShowUserProfileRequestParameterDto(userCode, userName, memberName, isDetail)
    }
  }

}
