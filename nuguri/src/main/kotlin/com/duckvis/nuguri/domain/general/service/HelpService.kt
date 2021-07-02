package com.duckvis.nuguri.domain.general.service

import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.types.nuguri.HelpOption
import com.duckvis.nuguri.dtos.ServiceRequestDto
import com.duckvis.nuguri.services.NuguriService
import com.duckvis.nuguri.services.ServicePermission
import org.springframework.stereotype.Service

/**
 * 도움말 기능 (!사용법)
 */
@Service("HELP")
class HelpService : NuguriService {
  override val minimumRequestParams = 0
  override val maximumRequestParams = 1
  override val minimumPermission = ServicePermission.MEMBER

  override fun response(serviceRequestDto: ServiceRequestDto): String {
    isValidCommand(serviceRequestDto)
    val helpOption = when (serviceRequestDto.params.size) {
      1 -> HelpOption.of(serviceRequestDto.params.single())
      0 -> HelpOption.HELP_OPTIONS
      else -> throw NuguriException(ExceptionType.TYPO)
    }

    return when (getPermission(serviceRequestDto.userLevel, serviceRequestDto.userTeam)) {
      ServicePermission.ADMIN -> adminHelpMessage(helpOption)
      ServicePermission.MANAGER -> managerHelpMessage(helpOption)
      ServicePermission.MEMBER -> normalHelpMessage(helpOption)
      else -> throw NuguriException(ExceptionType.USER_EXIT)
    }
  }

  private fun adminHelpMessage(option: HelpOption): String {
    return managerHelpMessage(option) + when (option) {
      HelpOption.ADMINISTRATION -> ""
      HelpOption.ATTENDANCE -> ""
      HelpOption.PROJECT -> ""
      HelpOption.STATISTICS -> ""
      HelpOption.HELP_OPTIONS -> ""
      HelpOption.ETC -> ""
    }
  }

  private fun managerHelpMessage(option: HelpOption): String {
    return normalHelpMessage(option) + when (option) {
      HelpOption.ADMINISTRATION -> {
        "\n\n\n아래는 매니저 권한으로 사용할 수 있는 명령어들이에요~\n\n\n" +
          "`!팀원등록 [팀명] [이름]`으로 팀에 팀원을 추가할 수 있어요~\n" +
          "`!팀원삭제 [팀명] [이름]`으로 팀에서 팀원을 삭제할 수 있어요~"
      }
      HelpOption.ATTENDANCE -> ""
      HelpOption.PROJECT -> {
        "\n\n\n아래는 매니저/관리자 권한으로 사용할 수 있는 명령어들이에요~\n\n\n" +
          "`!플젝시작 [플젝이름] [플젝별칭]`으로 프로젝트를 시작할 수 있어요~\n" +
          "`!플젝종료 [플젝이름]`으로 프로젝트를 종료할 수 있어요~\n"
      }
      HelpOption.STATISTICS -> ""
      HelpOption.HELP_OPTIONS -> ""
      HelpOption.ETC -> ""
    }
  }

  private fun normalHelpMessage(option: HelpOption): String {
    return when (option) {
      HelpOption.ADMINISTRATION -> {
        "`!입사`로 사원으로 등록할 수 있어요~\n\n" +
          "`!팀목록`, `!팀장목록`으로 팀목록, 팀장목록을 볼 수 있어요~\n" +
          "`!팀정보 [팀명]`, `!내팀`으로도 팀 정보를 볼 수 있어요~"
      }
      HelpOption.ATTENDANCE -> {
        "`!출근 [플젝명]`, `!출근 [플젝명]_[서브플젝명]으로 출근해요~\n" +
          "`!출근` 대신 `ㄱ`, `ㄲ`, `ㄱㄱ`라고 입력해도 돼요~\n" +
          "`%야간`,`%연장`,`%휴일` 옵션도 붙일 수 있어요~\n" +
          "`ㄲ`,`ㄱ`,`ㄱㄱ`만 입력하실 경우 기본 프로젝트로 자동 출근됩니다~\n\n\n" +
          "`몇시간?`,`ㅁㅅㄱ`,`ㅁㅅ`으로 내가 지금 몇시간째 근무중인지도 알 수 있어요~\n\n\n" +
          "`!퇴근`으로 퇴근해요~\n" +
          "`ㅃ`,`ㅂㅂ`라고 입력해도 돼요~\n\n\n" +
          "`!실수 [플젝명] [시간]`,`!실수 [플젝명]_[서브플젝명] [시간]`으로 근무시간을 수정할 수 있어요~\n" +
          "`!실수` 대신 `ㅠㅠ`라고 입력해도 돼요~\n" +
          "`%야간`,`%연장`,`%휴일` 옵션도 붙일 수 있어요~\n" +
          "맨 뒤에 날짜(yyMMdd)를 붙이면 해당 날짜로 수정돼요~\n\n\n" +
          "`지금?`,`ㅈㄱ`을 입력하면 지금 근무중인 사람을 보여줘요~\n" +
          "`^기본`처럼 `^플젝명`도 붙여보세요~"
      }
      HelpOption.PROJECT -> "`!플젝목록`으로 플젝 목록 확인이 가능해요~"
      HelpOption.STATISTICS -> {
        "본인의 통계만 확인 가능해요~\n" +
          "대신, 관리자나 매니저는 권한이 있는 모든 사람의 통계를 알려줄게요~\n\n\n" +
          "`!월간통계`,`!주간통계`라고 치거나\n" +
          "`!통계 210524 210525`,`!통계 0601`이라고 치면\n" +
          "해당하는 기간의 통계를 계산해 드려요~\n\n\n" +
          "`%야간`,`%연장`,`%휴일` 옵션도 붙일 수 있어요~\n" +
          "또, `^[플젝명]`,`^[팀명]`,`^[사원명]`,`^[전체]` 같은 것도 되니까 요것저것 붙여보길 바라요~"
      }
      HelpOption.HELP_OPTIONS -> "`!도움말 %플젝,%사원관리,%출퇴근,%통계,%기타`로 입력부탁드려요~ 권한에 맞는 도움말을 보여드릴게요~\n\n:closed_book:가이드북:closed_book: https://bit.ly/duckvis_attendance"
      HelpOption.ETC -> {
        "`!정보 [이름]`,`!세부정보 [이름]`,`!내정보`,`!내세부정보`도 해보세요~\n" +
          "`!정보변경 생일 0601`, `!정보변경 상메 나는행복합니다`으로 자기 정보도 관리할 수 있어요~\n\n\n" +
          "`!휴가 3`, `!병가 2` 등으로 그 달의 휴가/병가를 3/2일 등록할 수 있어요~\n" +
          "그럼 관리자 분이 `!달정보 6`으로 6월의 정보를 확인해서 여러분의 휴가를 확인할 거에요~"
      }
    }
  }
}