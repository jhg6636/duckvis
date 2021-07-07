package com.duckvis.core.types.nuguri.service

import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException

// THINKING responseMessage 추가?
// 1 service - 1 parser - 1 requestDto
enum class CommandMinorType(
  val commands: List<String>,
  val basePermission: NuguriServicePermission,
  val commandMajorType: CommandMajorType,
  val minimumParams: Int,
  val maximumParams: Int,
) {

  /**
   * general
   */
  HELP(
    commands = listOf("!사용법"),
    basePermission = NuguriServicePermission.MEMBER,
    commandMajorType = CommandMajorType.GENERAL,
    minimumParams = 0,
    maximumParams = 1,
  ),

  /**
   * attendance
   */
  HOW_LONG_I_WORKED(
    commands = listOf(
      "ㅁㅅㄱ?",
      "ㅁㅅㄱ",
      "ㅁㅅ?",
      "ㅁㅅ",
      "몇시간?",
      "몇시간",
      "atr",
      "Atr",
      "at",
      "At",
      "at?",
      "At?",
      "Atr?",
      "atr?"
    ),
    basePermission = NuguriServicePermission.MEMBER,
    commandMajorType = CommandMajorType.ATTENDANCE,
    minimumParams = 0,
    maximumParams = 0,
  ),
  LAST_LOGIN(
    commands = listOf("!마지막출근"),
    basePermission = NuguriServicePermission.MEMBER,
    commandMajorType = CommandMajorType.ATTENDANCE,
    minimumParams = 0,
    maximumParams = 0,
  ),
  LOGIN(
    commands = listOf("ㄱ", "ㄱㄱ", "ㄲ", "!출근", "r", "rr", "R"),
    basePermission = NuguriServicePermission.MEMBER,
    commandMajorType = CommandMajorType.ATTENDANCE,
    minimumParams = 0,
    maximumParams = 4,
  ),
  LOGOUT(
    commands = listOf("ㅃ", "ㅂㅂ", "ㅂ", "q", "qq", "Q", "!퇴근"),
    basePermission = NuguriServicePermission.MEMBER,
    commandMajorType = CommandMajorType.ATTENDANCE,
    minimumParams = 0,
    maximumParams = 0,
  ),
  MISTAKE(
    commands = listOf("!실수", "ㅠㅠ", "!ㅠㅠ"),
    basePermission = NuguriServicePermission.MEMBER,
    commandMajorType = CommandMajorType.ATTENDANCE,
    minimumParams = 0,
    maximumParams = 6,
  ),
  NOW(
    commands = listOf("ㅈㄱ", "지금?", "ㅈㄱ?", "wr", "wr?", "Wr", "Wr?", "지금"),
    basePermission = NuguriServicePermission.MEMBER,
    commandMajorType = CommandMajorType.ATTENDANCE,
    minimumParams = 0,
    maximumParams = 2,
  ),

  /**
   * statistics
   */
  STATISTICS(
    commands = listOf("!월간통계", "!ㅇㄱㅌㄱ", "!drxr", "!주간통계", "!ㅈㄱㅌㄱ", "!wrxr", "!통계", "!ㅌㄱ", "!xr"),
    basePermission = NuguriServicePermission.MEMBER,
    commandMajorType = CommandMajorType.STATISTICS,
    minimumParams = 0,
    maximumParams = 9,
  ),
  CORE_TIME_STATISTICS(
    commands = listOf("!코어타임"),
    basePermission = NuguriServicePermission.ADMIN,
    commandMajorType = CommandMajorType.STATISTICS,
    minimumParams = 0,
    maximumParams = 1,
  ),
  CSV_EXPORT(
    commands = listOf("!익스포트"),
    basePermission = NuguriServicePermission.ADMIN,
    commandMajorType = CommandMajorType.STATISTICS,
    minimumParams = 2,
    maximumParams = 3,
  ),

  /**
   * admin
   */
  // holiday
  SET_HOLIDAYS(
    commands = listOf("!쉬는날"),
    basePermission = NuguriServicePermission.ADMIN,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 1,
    maximumParams = 1,
  ),
  SET_WORKDAYS(
    commands = listOf("!일하는날"),
    basePermission = NuguriServicePermission.ADMIN,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 1,
    maximumParams = 1,
  ),
  THIS_MONTH_INFO(
    commands = listOf("!이번달", "!달정보"),
    basePermission = NuguriServicePermission.ADMIN,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 0,
    maximumParams = 2,
  ),

  // user
  ADD_ADMIN(
    commands = listOf("!관리자추가"),
    basePermission = NuguriServicePermission.ADMIN,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 1,
    maximumParams = 1,
  ),
  ADMIN_LIST(
    commands = listOf("!관리자목록"),
    basePermission = NuguriServicePermission.MEMBER,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 0,
    maximumParams = 0,
  ),
  DELETE_ADMIN(
    commands = listOf("!관리자삭제"),
    basePermission = NuguriServicePermission.ADMIN,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 1,
    maximumParams = 1,
  ),
  MEMBER_EXIT(
    commands = listOf("!퇴사"),
    basePermission = NuguriServicePermission.ADMIN,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 1,
    maximumParams = 1,
  ),
  REGISTER_USER(
    commands = listOf("!입사"),
    basePermission = NuguriServicePermission.EVERYBODY,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 0,
    maximumParams = 0,
  ),

  // profile
  CHANGE_PROFILE(
    commands = listOf("!정보변경"),
    basePermission = NuguriServicePermission.MEMBER,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 2,
    maximumParams = 2,
  ),
  SET_DAY_OFF(
    commands = listOf("!휴가"),
    basePermission = NuguriServicePermission.MEMBER,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 1,
    maximumParams = 1,
  ),
  SET_SICK_DAY_OFF(
    commands = listOf("!병가"),
    basePermission = NuguriServicePermission.MEMBER,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 1,
    maximumParams = 1,
  ),
  SHOW_USER_PROFILE(
    commands = listOf("!정보", "!내정보", "!세부정보", "!내세부정보"),
    basePermission = NuguriServicePermission.MEMBER,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 0,
    maximumParams = 1,
  ),

  // project
  ADD_SUB_PROJECT(
    commands = listOf("!서브플젝추가"),
    basePermission = NuguriServicePermission.MEMBER,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 3,
    maximumParams = 3,
  ),
  DELETE_SUB_PROJECT(
    commands = listOf("!서브플젝제거"),
    basePermission = NuguriServicePermission.MANAGER,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 2,
    maximumParams = 2,
  ),
  PROJECT_END(
    commands = listOf("!플젝종료"),
    basePermission = NuguriServicePermission.MANAGER,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 1,
    maximumParams = 1,
  ),
  PROJECT_LIST(
    commands = listOf("!플젝목록"),
    basePermission = NuguriServicePermission.MEMBER,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 0,
    maximumParams = 0,
  ),
  PROJECT_START(
    commands = listOf("!플젝시작"),
    basePermission = NuguriServicePermission.MANAGER,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 2,
    maximumParams = 2,
  ),
  SUB_PROJECT_LIST(
    commands = listOf("!서브플젝목록"),
    basePermission = NuguriServicePermission.MEMBER,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 1,
    maximumParams = 1,
  ),

  // team
  ADD_MANAGER(
    commands = listOf("!매니저추가"),
    basePermission = NuguriServicePermission.ADMIN,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 2,
    maximumParams = 2,
  ),
  ADD_TEAM_MEMBER(
    commands = listOf("!팀원등록"),
    basePermission = NuguriServicePermission.MANAGER,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 2,
    maximumParams = 2,
  ),
  ADD_TEAM(
    commands = listOf("!팀생성"),
    basePermission = NuguriServicePermission.ADMIN,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 2,
    maximumParams = 2,
  ),
  CHANGE_TEAM_NAME(
    commands = listOf("!팀명변경"),
    basePermission = NuguriServicePermission.ADMIN,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 2,
    maximumParams = 2,
  ),
  DELETE_MANAGER(
    commands = listOf("!매니저삭제"),
    basePermission = NuguriServicePermission.ADMIN,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 2,
    maximumParams = 2,
  ),
  DELETE_TEAM_MEMBER(
    commands = listOf("!팀원삭제"),
    basePermission = NuguriServicePermission.MANAGER,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 2,
    maximumParams = 2,
  ),
  DELETE_TEAM(
    commands = listOf("!팀삭제"),
    basePermission = NuguriServicePermission.ADMIN,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 1,
    maximumParams = 1,
  ),
  MANAGER_LIST(
    commands = listOf("!매니저목록"),
    basePermission = NuguriServicePermission.MEMBER,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 0,
    maximumParams = 0,
  ),
  MY_TEAM(
    commands = listOf("!내팀"),
    basePermission = NuguriServicePermission.MEMBER,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 0,
    maximumParams = 0,
  ),
  TEAM_INFO(
    commands = listOf("!팀정보"),
    basePermission = NuguriServicePermission.MEMBER,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 1,
    maximumParams = 1,
  ),
  TEAM_LIST(
    commands = listOf("!팀목록"),
    basePermission = NuguriServicePermission.MEMBER,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 0,
    maximumParams = 0,
  ),
  MEMBER_LIST(
    commands = listOf("!사원목록"),
    basePermission = NuguriServicePermission.MEMBER,
    commandMajorType = CommandMajorType.ADMIN,
    minimumParams = 0,
    maximumParams = 0,
  ),
  NOTHING(
    commands = listOf(),
    basePermission = NuguriServicePermission.EVERYBODY,
    commandMajorType = CommandMajorType.GENERAL,
    minimumParams = 0,
    maximumParams = 0,
  )
  ;

  companion object {
    /**
     * @param split된 text의 맨 앞 것
     * @return 해당하는 commandType, 없을 경우 null
     */
    fun of(command: String): CommandMinorType? {
      return values().singleOrNull { commandMinorType ->
        commandMinorType.commands.contains(command)
      }
    }
  }

  fun paramNumberCheck(splitText: List<String>) {
    if (splitText.size - 1 < minimumParams || splitText.size - 1 > maximumParams) {
      throw NuguriException(ExceptionType.TYPO)
    }
  }

}