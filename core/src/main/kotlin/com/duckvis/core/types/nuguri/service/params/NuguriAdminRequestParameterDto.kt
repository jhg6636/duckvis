package com.duckvis.core.types.nuguri.service.params

import com.duckvis.core.types.nuguri.service.CommandMinorType
import com.duckvis.core.utils.dayToDates
import java.time.LocalDate

data class NuguriAdminRequestParameterDto(
  override val userCode: String,
  override val userName: String,
  val teamName: String? = null,
  val targetUserName: String? = null,
  val newTeamName: String? = null,
  val projectName: String? = null,
  val projectNickname: String? = null,
  val subProjectName: String? = null,
  val subProjectNickname: String? = null,
  val dates: List<LocalDate> = listOf(),
  val profileChangeField: String? = null,
  val profileChangeContent: String? = null,
) : NuguriServiceRequestParameterDto(userCode, userName) {

  companion object {
    // TODO splitText param 개수 check
    fun of(
      splitText: List<String>,
      targetUserName: String,
      userCode: String,
      userName: String
    ): NuguriAdminRequestParameterDto {
      val commandType = CommandMinorType.of(splitText[0])
      val projectName = when (commandType) {
        CommandMinorType.PROJECT_END, CommandMinorType.PROJECT_START, CommandMinorType.SUB_PROJECT_LIST -> splitText[1]
        CommandMinorType.DELETE_SUB_PROJECT -> splitText[2]
        CommandMinorType.ADD_SUB_PROJECT -> splitText[3]
        else -> null
      }
      val projectNickname = when (commandType) {
        CommandMinorType.PROJECT_START -> splitText[2]
        else -> null
      }
      val subProjectName = when (commandType) {
        CommandMinorType.ADD_SUB_PROJECT, CommandMinorType.DELETE_SUB_PROJECT -> splitText[1]
        else -> null
      }
      val subProjectNickname = when (commandType) {
        CommandMinorType.ADD_SUB_PROJECT -> splitText[2]
        else -> null
      }
      val teamName = when (commandType) {
        CommandMinorType.REGISTER_USER, CommandMinorType.DELETE_TEAM_MEMBER,
        CommandMinorType.ADD_TEAM, CommandMinorType.DELETE_TEAM,
        CommandMinorType.CHANGE_TEAM_NAME, CommandMinorType.ADD_MANAGER,
        CommandMinorType.DELETE_MANAGER, CommandMinorType.TEAM_INFO -> splitText[1]
        else -> null
      }
      val newTeamName = when (commandType) {
        CommandMinorType.CHANGE_TEAM_NAME -> splitText[2]
        else -> null
      }
      val dtoUserName = when (commandType) {
        CommandMinorType.REGISTER_USER -> targetUserName
        CommandMinorType.MEMBER_EXIT, CommandMinorType.ADD_ADMIN, CommandMinorType.DELETE_ADMIN -> splitText[1]
        CommandMinorType.ADD_TEAM_MEMBER, CommandMinorType.DELETE_TEAM_MEMBER,
        CommandMinorType.ADD_TEAM, CommandMinorType.ADD_MANAGER, CommandMinorType.DELETE_MANAGER -> splitText[2]
        else -> null
      }
      val dates = when (commandType) {
        CommandMinorType.SET_HOLIDAYS, CommandMinorType.SET_WORKDAYS -> splitText[1].dayToDates
        else -> listOf()
      }
      val profileChangeField = when (commandType) {
        CommandMinorType.CHANGE_PROFILE -> splitText[1]
        else -> null
      }
      val profileChangeContent = when (commandType) {
        CommandMinorType.CHANGE_PROFILE -> splitText[2]
        else -> null
      }


      return NuguriAdminRequestParameterDto(
        userCode,
        userName,
        teamName,
        dtoUserName,
        newTeamName,
        projectName,
        projectNickname,
        subProjectName,
        subProjectNickname,
        dates,
        profileChangeField,
        profileChangeContent
      )
    }
  }

}
