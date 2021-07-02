package com.duckvis.core.exceptions.nuguri

import com.duckvis.core.SlackConstants

enum class ExceptionType(val message: String) {
  ALREADY_ADMIN("이미 관리자로 등록되어 있는 유저에요~"),
  ALREADY_ATTENDED("이미 근무중인 것 같은데요~?"),
  ALREADY_HOLIDAY("이미 휴일로 지정되어 있어요~"),
  ALREADY_TEAM_MANAGER("이미 매니저로 등록되어 있는 유저에요~"),
  CANNOT_CHANGE("이미 입력되어 있는 정보에요~"),
  DISCOUNT_OPTION_TYPO("\"휴가\" 혹은 \"급여\"라고 정확히 다시 입력해 주세요~"),
  NEVER_ATTENDED("사원님은 출근한 기록이 없어요~"),
  NOBODY_HAS_WORKED("코어타임 통계에요~\n아무도 코어타임에 일하지 않았어요~"),
  NOBODY_IS_WORKING(
    ":bell::bell:땡땡땡! 1시에요 1시! 코어타임 시작이에요~:bell::bell:" +
      "<@${SlackConstants.NUGURI_CHANNEL}> 아무도 출근하지 않으셨네요~\n모두 얼른 출근 부탁할게요~"
  ),
  NO_PERMISSION("해당 명령어에 대한 권한이 없어요~"),
  NO_SUCH_HOLIDAY("휴일이 아닌 날은 포함하면 안돼요~\n```!일하는날 DD,DD,DD``` 포맷으로 입력해 주세요~"),
  NO_SUCH_PROJECT("플젝명/플젝별명을 확인해 주세요~"),
  NO_SUCH_TEAM("팀명을 확인해 주세요~"),
  NO_SUCH_USER("사원명을 확인해 주세요~"),
  NOT_ADMIN("관리자가 아닌 사람은 삭제할 수 없어요~"),
  NOT_ENOUGH_MANAGER("이 분은 팀의 유일한 매니저라 삭제할 수 없어요~"),
  NOT_MANAGER("매니저가 아닌 사람은 삭제할 수 없어요~"),
  NOT_TEAM_MEMBER("해당하는 팀의 팀원이 아니에요~"),
  NOT_WORKING("사원님은 근무중이 아니에요~"),
  PROJECT_NAME_ALREADY_EXISTS("이미 있는 플젝명이에요~"),
  PROJECT_NICKNAME_ALREADY_EXISTS("이미 있는 플젝별칭이에요~"),
  TEAM_MEMBER_ALREADY_EXISTS("이미 팀에 포함되어 있는 사원이에요~"),
  TEAM_NAME_ALREADY_EXISTS("이미 있는 팀명이에요~"),
  TYPO("뭔가 잘못 입력하셨어요~ ```!도움말 (%플젝, %사원관리, %출퇴근, %통계, %기타)``` 옵션을 사용해 보는건 어떠세요~?"),
  USER_EXIT("그곳은 어떠신가요~~ 잘 지내시나요~~ 행복하신가요~?"),
  USER_PROFILE_CHANGE_TYPE_TYPO("변경하고자 하는 항목을 정확히 입력해주세요~\n변경은 성별, 생일, 입사일, 상태메시지(상메), 근무정책, 근무목표만 가능해요~"),
  WEEKEND("주말이 포함되어 있어요~ ```!쉬는날, !일하는날``` 설정에 주말이 포함되지 않게 해 주세요~"),
  MORE_THAN_ONE_DAY_MISTAKE("24시간 이상의 시간은 실수로 입력하실 수 없어요~"),
  OVER_MONTH_LENGTH("이번 달이 며칠까지 있는지 다시 한 번 확인해 주세요~"),
  NATIONAL_HOLIDAY(
    "양력 법정 공휴일이 포함되어 있어요~ ```!쉬는날, !일하는날``` 설정에 양력 법정 공휴일이 포함되지 않게 해주세요~\n" +
      ":spiral_calendar_pad:양력 법정공휴일 목록\n:sunrise:1/1\n:kr:3/1\n:child:5/5\n" +
      ":military_helmet:6/6\n:kr:8/15\n:fog:10/3\n:kr:10/9\n:evergreen_tree:12/25"
  ),
  MONTH_TYPO("몇 월인지 1부터 12까지의 숫자 중 하나로 알려주세요~"),
  COMMAND_TYPO("Need Fix"),
  SUB_PROJECT_NAME_OR_NICKNAME_ALREADY_EXISTS("서브플젝의 이름 또는 별명이 이미 존재해요~"),
  NO_SUCH_SUB_PROJECT("서브플젝명/서브플젝별명 또는 상위플젝명을 확인해 주세요~"),
  SUB_PROJECT_HAS_NO_PROJECT("서브플젝 상위의 플젝을 확인해 주세요~"),
  MISTAKE_FUTURE("미래의 날짜에 실수(ㅠㅠ)를 입력할 수 없어요~")
}