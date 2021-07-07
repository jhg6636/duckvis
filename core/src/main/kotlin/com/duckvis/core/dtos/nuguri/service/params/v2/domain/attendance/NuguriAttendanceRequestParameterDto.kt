package com.duckvis.core.dtos.nuguri.service.params.v2.domain.attendance

import com.duckvis.core.dtos.nuguri.WorkTypeDto
import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.core.dtos.nuguri.service.params.NuguriServiceRequestParameterDto
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.core.utils.toDurationSeconds
import java.time.LocalDate

data class NuguriAttendanceRequestParameterDto(
  override val userCode: String,
  override val userName: String,
  val projectNameOrNickname: String? = null,
  val subProjectNameOrNickname: String? = null,
  val mistakeSeconds: Int? = null,
  val workTypeDto: WorkTypeDto = WorkTypeDto(),
  val mistakeDate: LocalDate?,
) : NuguriServiceRequestParameterDto(userCode, userName) {

  companion object {
    fun of(splitText: List<String>, userCode: String, userName: String): NuguriAttendanceRequestParameterDto {
      val command = CommandMinorType.of(splitText.first())
      val projectAndSubProject = when (command) {
        CommandMinorType.LOGIN, CommandMinorType.MISTAKE -> splitText.getOrNull(1)?.split("_") ?: listOf("기본")
        else -> listOf()
      }
      val projectNameOrNickname = when (projectAndSubProject.size) {
        0 -> null
        else -> projectAndSubProject.first()
      }
      val subProjectNameOrNickname = when (projectAndSubProject.size) {
        2 -> projectAndSubProject.last()
        else -> null
      }
      val mistakeSeconds = when (command) {
        CommandMinorType.MISTAKE -> splitText[2].toDurationSeconds
        else -> null
      }
      val workTypeDto = WorkTypeDto.of(splitText)
      val mistakeDate = when {
        command == CommandMinorType.MISTAKE && splitText.size == 4 -> DateTimeMaker.stringToDate(splitText[3])
        else -> DateTimeMaker.nowDate()
      }

      return NuguriAttendanceRequestParameterDto(
        userCode,
        userName,
        projectNameOrNickname,
        subProjectNameOrNickname,
        mistakeSeconds,
        workTypeDto,
        mistakeDate
      )
    }
  }

}