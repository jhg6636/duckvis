package com.duckvis.slack

import com.duckvis.bob.dtos.MenuList
import com.duckvis.bob.dtos.TicketDto
import com.duckvis.bob.services.*
import com.duckvis.bob.ui.command.*
import com.duckvis.core.SlackConstants.Companion.BOB_CHANNEL
import com.duckvis.core.SlackConstants.Companion.NUGURI_CHANNEL
import com.duckvis.core.SlackConstants.Companion.NUGURI_POND_CHANNEL
import com.duckvis.core.domain.bob.BobTicket
import com.duckvis.core.domain.bob.BobTicketQueryDslRepository
import com.duckvis.core.domain.nuguri.UserProfile
import com.duckvis.core.domain.nuguri.UserProfileRepository
import com.duckvis.core.domain.nuguri.UserTeam
import com.duckvis.core.domain.nuguri.UserTeamRepository
import com.duckvis.core.domain.shared.User
import com.duckvis.core.domain.shared.UserRepository
import com.duckvis.core.exceptions.NotMatchingPayTypeException
import com.duckvis.core.exceptions.bob.*
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.duckvis.core.exceptions.shared.UserAlreadyLivesThereException
import com.duckvis.core.types.bob.BobStyleType
import com.duckvis.core.types.bob.BobTimeType
import com.duckvis.core.types.nuguri.service.CommandMajorType
import com.duckvis.core.types.shared.CityType
import com.duckvis.core.types.shared.UserPathType
import com.duckvis.core.utils.DateTimeMaker
import com.duckvis.core.utils.splitByMaximumLetterCount
import com.duckvis.core.utils.splitFirst
import com.duckvis.nuguri.domain.statistics.service.StatisticsService
import com.duckvis.nuguri.repository.UserTeamNuguriRepository
import com.duckvis.nuguri.services.NuguriServiceV2
import com.duckvis.nuguri.ui.CommandParserV2
import com.duckvis.nuguri.ui.CommandType
import com.duckvis.slack.dtos.SlackRequestDto
import com.duckvis.slack.service.GetUserNameService
import com.duckvis.slack.service.PostMessageService
import com.google.gson.JsonParser
import lombok.extern.slf4j.Slf4j
import org.hibernate.exception.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.time.DateTimeException
import java.time.format.DateTimeParseException
import kotlin.random.Random

// TODO BobController, NuguriController 분리
// CommandType 통합 및 안에 PurposeType (BOB, NUGURI)로 처리하면 될듯?

@RestController
@Slf4j
class SlackController(
  private val bobCommandParser: List<CommandParser<*>>,
  private val nuguriServices: Map<String, NuguriServiceV2>,
  private val postMessageService: PostMessageService,
  private val getUserNameService: GetUserNameService,
  private val userRepository: UserRepository,
  private val userProfileRepository: UserProfileRepository,
  private val userTeamNuguriRepository: UserTeamNuguriRepository,
  private val bobTicketService: BobTicketService,
  private val bobMenuService: BobMenuService,
  private val bobGeneralService: BobGeneralService,
  private val bobTeamService: BobTeamService,
  private val bobMoneyService: BobMoneyService,
  private val ticketRepository: BobTicketQueryDslRepository,
  private val userTeamRepository: UserTeamRepository,
) {

  private val log = LoggerFactory.getLogger(this.javaClass)

  @GetMapping("/hello")
  @ResponseBody
  fun hello(): String {
    return "hello, I'm duckvis ${DateTimeMaker.nowDateTime()}"
  }

//    @PostMapping("slack/event")
//    fun challenge(@RequestBody challengeMessage: String): String {
//        var jsonObject = JsonParser.parseString(challengeMessage).asJsonObject
//        return jsonObject.get("challenge").toString()
//    }

  @Transactional
  @PostMapping("slack/event")
  fun slackEvents(@RequestBody body: SlackRequestDto) {
    val slackEventDto = body.slackEventDto.trim()

    val bobCommand = textToBobCommand(slackEventDto.text)
    /**
     * 너굴
     */
    if (bobCommand == null) {
      if (slackEventDto.channel == BOB_CHANNEL) {
        return
      }
      log.info(slackEventDto.toString())
      val finalMessage = try {
        nuguriSlackResponse(slackEventDto.text, slackEventDto.userCode, slackEventDto.channel) ?: return
      } catch (e: NuguriException) {
        e.type.message
      } catch (e: DateTimeParseException) {
        "날짜의 형식은 MMDD, 시간의 형식은 HH 혹은 HH:mm이니 지켜주세요~"
      } catch (e: NullPointerException) {
        "널포인터익셉션"
      } catch (e: DateTimeException) {
        "날짜/시간을 다시 확인해 주세요~"
      } catch (e: Exception) {
        e.printStackTrace()
        "개발자 문의 주세요"
      }

      finalMessage.splitByMaximumLetterCount(1500)
      postMessageService.post(finalMessage, slackEventDto.channel)
    }
    /**
     * 밥
     */
    else {
      bobSlackResponse(slackEventDto.text, slackEventDto.userCode, slackEventDto.channel)
    }
  }

  fun textToBobCommand(text: String): BobCommand? {
    this.bobCommandParser.forEach {
      val bobCommand = it.fromText(text)
      if (bobCommand != null) {
        return bobCommand
      }
    }
    return null
  }

  fun nuguriSlackResponse(
    text: String,
    userCode: String,
    channel: String,
    pathType: UserPathType = UserPathType.SLACK
  ): String? {
    val user = userRepository.findByCodeAndPath(userCode, pathType)
      ?: userRepository.save(User(userCode, getUserNameService.get(userCode)))

    checkUserProfile(user)

    if (user.isBot) {
      return null
    }

    if (channel == BOB_CHANNEL) {
      return null
    }

    val commandParser = CommandParserV2()
    val serviceRequestDto = commandParser.extractRequestParams(
      text.split(" "),
      user.name,
      user.code,
      getHighestUserTeam(user),
      user.isAdmin,
      user.isGone
    )
    val nuguriService = getNuguriService(text)
    val isManager = userTeamNuguriRepository.isAnyTeamManager(user.id)
    if (nuguriService is StatisticsService && (channel == NUGURI_CHANNEL || channel == NUGURI_POND_CHANNEL)) {
      return "통계 명령어는 DM을 통해서 입력 부탁드려요~"
    }

    return if (isBlockedAttendanceCommandByDM(channel, nuguriService, user, isManager)) {
      "해당 명령어는 관리자만 사용할 수 있거나, 기타_출퇴근방에서만 사용할 수 있어요~"
    } else {
      nuguriService.response(serviceRequestDto)
    }
  }

  @Transactional
  fun checkUserProfile(user: User) {
    if (userProfileRepository.findByUserId(user.id) == null) {
      userProfileRepository.save(UserProfile(user.id, user.name))
    }
  }

  fun getHighestUserTeam(user: User): UserTeam {
    val userTeams = userTeamRepository.findAllByUserId(user.id)
    return userTeams.firstOrNull { userTeam -> userTeam.isManager } ?: userTeams.firstOrNull() ?: UserTeam(user.id, 0L)
  }

  // THINKING 1) 메소드 이름이 이상하다 - 수정 완 2) aboutAttendance 이상하다 - V2에서는 commandType 확인
  private fun isBlockedAttendanceCommandByDM(
    channel: String,
    nuguriService: NuguriServiceV2,
    user: User,
    isManager: Boolean
  ): Boolean {
    return channel == user.code && nuguriService.type.commandMajorType == CommandMajorType.ATTENDANCE && !(user.isAdmin || isManager)
  }

  private fun getNuguriService(text: String): NuguriServiceV2 {
    log.info(CommandType.from(text.splitFirst()).name + "_V2")
    return nuguriServices[CommandType.from(text.splitFirst()).name + "_V2"]
      ?: throw NuguriException(ExceptionType.COMMAND_TYPO)
  }

  fun bobSlackResponse(text: String, userCode: String, channel: String, pathType: UserPathType = UserPathType.SLACK) {
    val bobCommand = textToBobCommand(text)
    try {
      val user = userRepository.findByCodeAndPath(userCode, pathType)
        ?: userRepository.save(User(userCode, getUserNameService.get(userCode)))
      val userId: Long = user.id
      if (user.isBot) {
        return
      }

      if (bobCommand !is ResponseTotalOverMoney && channel == userCode) { // Command in DM
        postMessageService.post("해당 명령어는 밥방에서 입력하셔야죠옹~", channel)
      } else if (channel != BOB_CHANNEL) { // 다른 채널에서 입력한 커맨드
        return
      }

      if (bobCommand is ResponseShowAllPays && user.isAdmin && channel == userCode) {
        val checkingUser = userRepository.findByName(bobCommand.userName) ?: throw NoSuchUserException()
        val userPays = bobMoneyService.responseShowAllPay(checkingUser.id, bobCommand.size)
        val message = "${checkingUser.name}님의 최근 ${bobCommand.size}회 식비지원 내역이에요~\n\n" +
          userPays.joinToString("\n") { userPay ->
            ":pushpin:${userPay.payString}"
          }
        postMessageService.post(message, userCode)
        return
      } else if (bobCommand is ResponseShowAllPays && channel != userCode) {
        return
      }

      when (bobCommand) {
        is ResponseMe -> {
          val mealType = BobTimeType.of(DateTimeMaker.nowDateTime())
          val str = ticketDtoToString(
            TicketDto(
              bobTicketService.responseMe(userId, BobStyleType.ANYTHING),
              ticketRepository.isFirst(mealType, BobStyleType.ANYTHING)
            )
          )
          postMessageService.post(str, channel)
        }
        is ResponseMeSpecificPlace -> {
          val mealType = BobTimeType.of(DateTimeMaker.nowDateTime())
          val str = ticketDtoToString(
            TicketDto(
              bobTicketService.responseMeSpecificPlace(userId, BobStyleType.ANYTHING, bobCommand.city),
              ticketRepository.isFirst(mealType, BobStyleType.ANYTHING)
            )
          )
          postMessageService.post(str, channel)
        }
        is ResponseVegetarian -> {
          val mealType = BobTimeType.of(DateTimeMaker.nowDateTime())
          val str = ticketDtoToString(
            TicketDto(
              bobTicketService.responseMe(userId, BobStyleType.VEGETARIAN),
              ticketRepository.isFirst(mealType, BobStyleType.VEGETARIAN)
            )
          )
          postMessageService.post(str, channel)
        }
        is ResponseVegetarianSpecificPlace -> {
          val mealType = BobTimeType.of(DateTimeMaker.nowDateTime())
          val str = ticketDtoToString(
            TicketDto(
              bobTicketService.responseMeSpecificPlace(userId, BobStyleType.VEGETARIAN, bobCommand.city),
              ticketRepository.isFirst(mealType, BobStyleType.VEGETARIAN)
            )
          )
          postMessageService.post(str, channel)
        }
        is ResponseLateEat -> postMessageService.post(
          ticketDtoToString(
            TicketDto(
              bobTicketService.responseMe(userId, BobStyleType.LATE_EAT),
              false
            )
          ),
          channel
        )
        is ResponseLateEatSpecificPlace -> postMessageService.post(
          ticketDtoToString(
            TicketDto(
              bobTicketService.responseMeSpecificPlace(userId, BobStyleType.LATE_EAT, bobCommand.city),
              false
            )
          ),
          channel
        )
        is ResponseAddMenu -> postMessageService.post(
          "철이 없었죠 ${bobMenuService.responseAddMenu(bobCommand.menu.name).name} 메뉴 추가도 안해놨다는게.. 고마워요 호호호호호",
          channel
        )
        is ResponseRemoveMenu -> {
          bobMenuService.responseRemoveMenu(bobCommand.menu.name)
          postMessageService.post("${bobCommand.menu.name} 메뉴가 제거되었어요옹", channel)
        }
        is ResponseAllMenu -> postMessageService.post(MenuList(bobMenuService.responseAllMenu()).toString(), channel)
        is ResponseStatistics -> postMessageService.post(
          bobGeneralService.responseStatistics(userId, bobCommand.option).toString(), channel
        )
        is ResponseNotMe -> {
          bobTicketService.responseNotMe(userId)
          postMessageService.post("밥 잘 챙겨 먹고 다녀요 니가 1그램이라도 사라지는건 싫으니까.", channel)
        }
        is ResponseChangeLivingCity -> {
          try {
            val changedCityName =
              bobGeneralService.responseChangeLivingPlace(userId, bobCommand.city).city.cityName
            postMessageService.post("이제 $changedCityName 사시네요 홓홓", channel)
          } catch (e: UserAlreadyLivesThereException) {
            postMessageService.post("이미 ${bobCommand.city.name} 사람이에요", channel)
          }
        }
        is ResponseHelp -> postMessageService.post(bobGeneralService.responseHelp(), channel)
        is ResponseRecommendMenu -> postMessageService.post(
          "오늘의 비스 추천 메뉴는 ${bobMenuService.responseRecommendMenu(bobCommand.exceptingMenus.map { it.name }).name}에요옹",
          channel
        )
        is ResponseDetermineBobTeam -> {
          val thisMealBobTeams = bobTeamService.determineBobTeam(CityType.SEOUL, bobCommand.mealType)
          thisMealBobTeams.teams.forEach { team ->
            team.asString.split("\n").forEach { str ->
              postMessageService.post(str, channel)
              Thread.sleep(Random.nextLong(500, 1000))
            }
          }
        }
        is ResponsePayWithSupport -> {
          val message = bobMoneyService.responsePayWithSupport(userId, bobCommand.money)
          postMessageService.post(message, userCode)
        }
        is ResponsePayWithoutSupport -> {
          val message = bobMoneyService.responsePayWithoutSupport(userId, bobCommand.money)
          postMessageService.post(message, userCode)
        }
        is ResponseSelfPayWithSupport -> {
          val message = bobMoneyService.responseSelfPayWithSupport(userId, bobCommand.deliveryCost)
          postMessageService.post(message, userCode)
        }
        is ResponseSelfPayWithoutSupport -> {
          val message = bobMoneyService.responseSelfPayWithoutSupport(userId, bobCommand.deliveryCost)
          postMessageService.post(message, userCode)
        }
        is ResponseModifyPayWithSupport -> {
          val message = bobMoneyService.responseModifyPayWithSupport(
            userId,
            bobCommand.bobTimeType,
            bobCommand.money,
            bobCommand.date
          )
          postMessageService.post(message, userCode)
        }
        is ResponseModifyPayWithoutSupport -> {
          val message = bobMoneyService.responseModifyPayWithoutSupport(
            userId,
            bobCommand.bobTimeType,
            bobCommand.money,
            bobCommand.date
          )
          postMessageService.post(message, userCode)
        }
        is ResponseModifySelfPayWithSupport -> {
          val message = bobMoneyService.responseModifySelfPayWithSupport(
            userId,
            bobCommand.bobTimeType,
            bobCommand.deliveryCost,
            bobCommand.date
          )
          postMessageService.post(message, userCode)
        }
        is ResponseModifySelfPayWithoutSupport -> {
          val message =
            bobMoneyService.responseModifySelfPayWithoutSupport(
              userId,
              bobCommand.bobTimeType,
              bobCommand.deliveryCost,
              bobCommand.date
            )
          postMessageService.post(message, userCode)
        }
        is ResponseTotalOverMoney -> {
          val total = bobMoneyService.responseTotalOverMoney(userId)
          postMessageService.post(
            "${getUserNameService.get(userCode)} 님의 현재까지 누적된 초과금은\n:moneybag:${total}원 이에요옹~",
            userCode
          )
        }
        null -> return
        else -> return
      }
      return
    } catch (e: BobTicketAlreadyExistsException) {
      postMessageService.post("배고파요? 조금만 기다려줘요~ 흐하핳", channel)
    } catch (e: ConstraintViolationException) {
      postMessageService.post("배고파요? 조금만 기다려줘요~ 흐하핳", channel)
    } catch (e: DataIntegrityViolationException) {
      postMessageService.post("배고파요? 조금만 기다려줘요~ 흐하핳", channel)
    } catch (e: NotTicketTimeException) {
      postMessageService.post("아직 밥 먹을 시간 아니에요 귀여운 꼬마 아가씨", channel)
    } catch (e: NoSuchPlaceException) {
      postMessageService.post("그게 어디에요? 알려줘요~~", channel)
    } catch (e: MenuAlreadyExistsException) {
      postMessageService.post("이미 있는 메뉴에요 이 바보~ ㅏ하하하하하핳", channel)
    } catch (e: NoMenuSavedException) {
      postMessageService.post("저장된 메뉴가 한 개도 없어요~", channel)
    } catch (e: NeverEatThisMonthException) {
      postMessageService.post("${getUserNameService.get(userCode)}님, 밥은 먹고 통계 확인하셔야죠 이 장난꾸러기! 하호호핳핳", channel)
    } catch (e: NoSuchMenuException) {
      postMessageService.post("없는 메뉴 지우면 어떡해요 이 바보~ ㅏ하하하하하핳", channel)
    } catch (e: NoBobTicketException) {
      postMessageService.post("으이그~ 밥 신청도 안 했으면서", channel)
    } catch (e: InvalidCommandException) {
      postMessageService.post("", channel)
    } catch (e: NotRegisteredUserException) {
      postMessageService.post("누구세요옹", channel)
    } catch (e: NotEnoughBobTicketException) {
      postMessageService.post("서울은 오늘 밥팀이 없어요옹~ 밥들 챙겨 먹고 다녀요.", channel)
    } catch (e: NullPointerException) {
      postMessageService.post("", channel)
    } catch (e: NumberFormatException) {
      postMessageService.post("금액에는 숫자만 입력해주세요 이 바보 하하하하핳ㅎ~", userCode)
      postMessageService.post("${getUserNameService.get(userCode)}님 이쪽으로 내려와 봐요옹~", channel)
    } catch (e: TooLateException) {
      postMessageService.post(
        "11~15시, 18~22시에 잘 입력해주세요옹~~ 혹시 늦으셨거나 수정이 필요하시면\n" +
          "```!금액 [액수] [%지원,미지원,개인결제] [yyMMDD] [점심,저녁]```\n를 입력해 주세요옹~", userCode
      )
      postMessageService.post("${getUserNameService.get(userCode)}님 이쪽으로 내려와 봐요옹~", channel)
    } catch (e: DateTimeParseException) {
      postMessageService.post("날짜의 형식은 MMDD 혹은 yyMMDD이니 지켜주세요옹~", userCode)
      postMessageService.post("${getUserNameService.get(userCode)}님 이쪽으로 내려와 봐요옹~", channel)
    } catch (e: DateTimeException) {
      postMessageService.post("날짜의 형식은 MMDD 혹은 yyMMDD이니 지켜주세요옹~", userCode)
      postMessageService.post("${getUserNameService.get(userCode)}님 이쪽으로 내려와 봐요옹~", channel)
    } catch (e: BobTimeStringException) {
      postMessageService.post("점심/저녁을 오타 없이 입력하셨는지 확인해주세요옹~", userCode)
      postMessageService.post("${getUserNameService.get(userCode)}님 이쪽으로 내려와 봐요옹~", channel)
    } catch (e: NotMatchingPayTypeException) {
      postMessageService.post(
        "혹시 법카결제 하셨는데 %개인카드 금액 입력을 하셨거나 개인카드 결제 하셨는데 %지원/미지원 금액 입력 하셨나요옹??\n" +
          "다시 한 번 확인해 주세요옹~", userCode
      )
      postMessageService.post("${getUserNameService.get(userCode)}님 이쪽으로 내려와 봐요옹~", channel)
    } catch (e: IllegalStateException) {
      println(e.cause?.javaClass)
      val str = when (e.cause?.javaClass) {
        BobTicketAlreadyExistsException::class.java -> "배고파요? 조금만 기다려줘요~ 흐하핳"
        NotRegisteredUserException::class.java -> "누구세요옹"
        else -> "개발자 문의"
      }
      postMessageService.post(str, channel)
    }
    return
  }

  @Transactional
  fun ticketDtoToString(ticketDto: TicketDto): String {
    if (ticketDto.bobTicket.isSelfPay) {
      postMessageService.post(
        "${ticketDto.bobTicket.userName}님은 초과금이 10,000원을 넘어서 오늘은 개인결제 하셔야 돼요옹~",
        ticketDto.bobTicket.userCode
      )
    }
    return if (ticketDto.isFirst) "${ticketDto.bobTicket.userName}님, 1등하셨네요 너무 귀엽다. ${ticketDto.bobTicket.city} ${ticketDto.bobTicket.bobTimeType.korean}" +
      "${ticketDto.bobTicket.bobStyle}, 맛있게 먹어요"
    else "${ticketDto.bobTicket.city} ${ticketDto.bobTicket.bobTimeType.korean}${ticketDto.bobTicket.bobStyle}, 맛있게 먹어요"
  }

  private val BobTicket.userCode: String
    get() = userRepository.findByIdOrNull(this.userId)?.code ?: throw NoSuchUserException()

}