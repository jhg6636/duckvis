package com.duckvis.bob.services

import com.duckvis.bob.dtos.StatisticsDto
import com.duckvis.core.domain.bob.BobHistoryQueryDslRepository
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.bob.NeverEatThisMonthException
import com.duckvis.core.exceptions.bob.NoSuchUserException
import com.duckvis.core.exceptions.bob.NotRegisteredUserException
import com.duckvis.core.types.bob.StatisticsOption
import com.duckvis.core.types.shared.CityType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@ComponentScan(basePackages = ["com.duckvis"])
class BobGeneralService(
  @Autowired private val userRepository: UserRepository,
  @Autowired private val bobHistoryQueryDslRepository: BobHistoryQueryDslRepository,
) {
  @Transactional
  fun responseChangeLivingPlace(userId: Long, city: CityType): User {
    val user = userRepository.findByIdOrNull(userId)
      ?: throw NotRegisteredUserException()
    user.changeCity(city)
    return user
  }

  fun responseHelp(): String {
    return "*밥 투표* : `저요`, `ㅈㅇ`\n" +
      "*투표 취소* : `안먹`, `ㅇㅁ`\n" +
      "기본 지역 설정 (서울, 대전 - 기본값은 서울입니다) : 이제 @@ 살래\n" +
      "오늘만 다른 지역에서 먹고 싶을 때 : `서울에서`, `대전에서`\n" +
      "*채식 투표* : `ㅊㅅ`, `채식`\n" +
      "오늘만 다른 지역에서 채식 : `ㄷㅈㅊㅅ`, `ㅅㅇㅊㅅ`, `대전채식`, `서울채식`\n" +
      "*업무상 늦게 먹어야할 때* : `!늦먹`, `ㄴㅁ`, `!대전늦먹`, `!ㄷㅈㄴㅁ`, `!서울늦먹`, `!ㅅㅇㄴㅁ`\n" +
      "메뉴추천: `ㅊㅊ`, 메뉴추천 (특정 메뉴 제외: `%제외 오이,피클,가지` / `%제외 오이`)\n" +
      "전체메뉴: `!메뉴`\n" +
      "메뉴추가: `!메뉴추가 돼지국밥`\n" +
      "메뉴제거: `!메뉴제거 돼지국밥`\n" +
      "`밥통계`, `ㅂㅌㄱ` (%밥부장, %짝꿍): 써보세요\n\n" +
      "(금액등록)\n" +
      "*점심*금액 *11시~16시*, *저녁*금액 *17시~22시* 당일 등록 시\n" +
      "*지원* : `!금액 9900`\n*미지원* : `!금액 9500 %미지원`\n" +
      "*개인결제(지원)* : `!배달비 750`\n" +
      "*개인결제(미지원)* : `!배달비 800 %미지원`\n" +
      "이외 시간에 금액 수정/등록 시\n" +
      "지원 : `!금액 9900 %지원 yymmdd(mmdd) 점심`\n" +
      "미지원 : `!금액 9000 %미지원 yymmdd(mmdd) 저녁`\n" +
      "개인결제(지원) : `!배달비 700 %지원 yymmdd(mmdd) 점심`\n" +
      "개인결제(미지원) : `!배달비 1300 %미지원 yymmdd(mmdd) 저녁`\n" +
      "등록된 금액에 대해서는 수정하시면 *자동으로 덮어쓰기 됩니다* (기존 거 삭제됨)\n" +
      "금액 등록은 팀별 스레드에 본인이 직접 입력해 주세요~\n" +
      "현재 본인의 초과금을 확인 : `!초과금`, `!내초과금`, `ㅊㄱㄱ` (DM으로 발송됩니다) (덕비스에게 DM으로 보내셔도 됩니다)\n\n" +
      "식비지원제도가 궁금하신 경우 bit.ly/duckvis_policy 를 방문해주세요!"
  }

  @Transactional
  fun responseStatistics(userId: Long, option: StatisticsOption): StatisticsDto {
    val bobLeaderCount = bobHistoryQueryDslRepository.bobLeaderCount(userId)
    val teammates = bobHistoryQueryDslRepository.teammates(userId)
      .map { teammateId -> userRepository.findByIdOrNull(teammateId) ?: throw NoSuchUserException() }
    val userName = userRepository.findByIdOrNull(userId)?.name ?: throw NotRegisteredUserException()
    val mostFrequentTeammate = teammates.groupBy { it }.mapValues { it.value.size }.maxByOrNull { it.value }
      ?: throw NeverEatThisMonthException()
    return StatisticsDto(
      userName,
      bobLeaderCount,
      mostFrequentTeammate.key.name,
      mostFrequentTeammate.value,
      option
    )
  }
}