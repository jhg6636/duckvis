package com.duckvis.nuguri.ui

enum class CommandType(val commands: List<String>) {
  HELP(listOf("!도움말")),
  PROJECT_LIST(listOf("!플젝목록")),
  PROJECT_END(listOf("!플젝종료")),
  PROJECT_START(listOf("!플젝시작")),
  REGISTER_USER(listOf("!입사")),
  TEAM_LIST(listOf("!팀목록")),
  MANAGER_LIST(listOf("!팀장목록", "!매니저목록")),
  TEAM_INFO(listOf("!팀정보")),
  MY_TEAM(listOf("!내팀")),
  ADD_TEAM_MEMBER(listOf("!팀원등록")),
  DELETE_TEAM_MEMBER(listOf("!팀원삭제")),
  MEMBER_EXIT(listOf("!퇴사")),
  ADD_TEAM(listOf("!팀생성")),
  DELETE_TEAM(listOf("!팀삭제")),
  CHANGE_TEAM_NAME(listOf("!팀명변경")),
  ADD_ADMIN(listOf("!관리자추가")),
  DELETE_ADMIN(listOf("!관리자삭제")),
  ADMIN_LIST(listOf("!관리자목록")),
  ADD_MANAGER(listOf("!매니저추가")),
  DELETE_MANAGER(listOf("!매니저삭제")),
  LOGIN(listOf("ㄱ", "ㄱㄱ", "ㄲ", "!출근", "r", "rr", "R")),
  LOGOUT(listOf("ㅃ", "ㅂ", "ㅂㅂ", "q", "qq", "Q", "!퇴근")),
  MISTAKE(listOf("!실수", "ㅠㅠ", "!ㅠㅠ")),
  NOW(listOf("ㅈㄱ", "지금?", "ㅈㄱ?", "wr", "wr?", "Wr", "Wr?", "지금")),
  HOW_LONG_I_WORKED(
    listOf(
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
    )
  ),
  LAST_LOGIN(listOf("!언제출근", "!마지막출근")),
  STATISTICS(listOf("!월간통계", "!ㅇㄱㅌㄱ", "!drxr", "!주간통계", "!ㅈㄱㅌㄱ", "!wrxr", "!통계", "!ㅌㄱ", "!xr")),
  CORE_TIME_STATISTICS(listOf("!코어타임")),
  CSV_EXPORT(listOf("!익스포트")),
  SHOW_USER_PROFILE(listOf("!정보", "!내정보", "!세부정보", "!내세부정보")),
  CHANGE_PROFILE(listOf("!정보변경")),
  THIS_MONTH_INFO(listOf("!이번달", "!달정보")),
  SET_DAY_OFF(listOf("!휴가")),
  SET_SICK_DAY_OFF(listOf("!병가")),
  SET_HOLIDAYS(listOf("!쉬는날")),
  SET_WORKDAYS(listOf("!일하는날")),
  ADD_SUB_PROJECT(listOf("!서브플젝추가")),
  DELETE_SUB_PROJECT(listOf("!서브플젝제거")),
  SUB_PROJECT_LIST(listOf("!서브플젝목록")),
  MEMBER_LIST(listOf("!사원목록")),
  NOTHING(listOf());

  companion object {

    fun of(string: String): CommandType? {
      values().forEach {
        if (it.commands.contains(string)) {
          return it
        }
      }
      return null
    }

    fun from(string: String): CommandType {
      values().forEach {
        if (it.commands.contains(string)) {
          return it
        }
      }
      return NOTHING
    }
  }

}
