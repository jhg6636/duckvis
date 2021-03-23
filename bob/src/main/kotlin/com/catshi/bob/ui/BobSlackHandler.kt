package com.catshi.bob.ui

import com.catshi.bob.domain.BobTicketQueryDslRepository
import com.catshi.bob.dtos.MenuList
import com.catshi.bob.dtos.SlackRequestDto
import com.catshi.bob.dtos.TicketDto
import com.catshi.bob.exceptions.*
import com.catshi.bob.services.*
import com.catshi.bob.types.BobStyleType
import com.catshi.bob.types.BobTimeType
import com.catshi.bob.types.CommandType
import com.catshi.bob.ui.command.*
import com.catshi.core.domain.User
import com.catshi.core.domain.UserRepository
import com.catshi.core.exceptions.UserAlreadyLivesThereException
import com.catshi.core.types.CityType
import com.catshi.core.types.CountryType
import com.catshi.core.types.UserPathType
import com.catshi.core.utils.TimeHandler
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.hibernate.exception.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange
import javax.annotation.PostConstruct

@RestController
@Controller
class BobSlackHandler(
    val bobService: BobService,
//    val userDao: UserDao,
    val ticketRepository: BobTicketQueryDslRepository,
    val userRepository: UserRepository,
    private val commandParsers: List<CommandParser<*>>
) {
    @GetMapping("/hello")
    @ResponseBody
    fun hello(): String {
        return "hello, I'm duckvis ${TimeHandler.nowDateTime()}"
    }

//    @PostMapping("slack/event")
//    fun challenge(@RequestBody challengeMessage: String): String {
//        var jsonObject = JsonParser.parseString(challengeMessage).asJsonObject
//        return jsonObject.get("challenge").toString()
//    }

    @Transactional
    @PostMapping("slack/event")
    fun slackEvents(@RequestBody body: String): String {
//        println("here")
//        println(body)
        val slackMessageDto = jsonBodyToDto(body)
        val postString = slackResponse(slackMessageDto.text, slackMessageDto.userCode)
        return postMessageRequest(postString).toString()
    }

    private fun jsonBodyToDto(body: String): SlackRequestDto {
        val firstJson = JsonParser.parseString(body).asJsonObject
        val eventJson = firstJson.get("event").toString()
        val secondJson = JsonParser.parseString(eventJson).asJsonObject
        val text = try {
            secondJson.get("text").toString()
        } catch (e: NullPointerException) {
            ""
        }
        val userCode = try {
            secondJson.get("user").toString()
        } catch (e: NullPointerException) {
            ""
        }
        return SlackRequestDto(text.trim('\"'),
                                userCode.trim('\"'))
    }

    fun textToCommand(text: String): BobCommand? {
        this.commandParsers.forEach {
            val bobCommand = it.fromText(text)
            if (bobCommand != null) {
                return bobCommand
            }
        }
        return null
    }

    fun slackResponse(text: String, userCode: String, pathType: UserPathType = UserPathType.SLACK): String {
//        println(text)
//        println(userCode)
        val userId: Long = userRepository.findByUserCodeAndPath(userCode, pathType)?.id
            ?: userRepository.save(User(userCode, getUserName(userCode))).id
        if (isBot(userId)) {
            return ""
        }

        val bobCommand = textToCommand(text)
//        println(bobCommand)

        try {
            when (bobCommand) {
                is ResponseMe -> {
                    val mealType = BobTimeType.of(TimeHandler.nowDateTime())
                    return TicketDto(bobService.responseMe(userId), ticketRepository.isFirst(mealType, BobStyleType.ANYTHING)).toString()
                }
                is ResponseMeSpecificPlace -> {
                    val mealType = BobTimeType.of(TimeHandler.nowDateTime())
                    return TicketDto(bobService.responseMeSpecificPlace(userId, bobCommand.city), ticketRepository.isFirst(mealType, BobStyleType.ANYTHING)).toString()
                }
                is ResponseVegetarian -> {
                    val mealType = BobTimeType.of(TimeHandler.nowDateTime())
                    return TicketDto(bobService.responseVegetarian(userId), ticketRepository.isFirst(mealType, BobStyleType.VEGETARIAN)).toString()
                }
                is ResponseVegetarianSpecificPlace -> {
                    val mealType = BobTimeType.of(TimeHandler.nowDateTime())
                    return TicketDto(bobService.responseVegetarianSpecificPlace(userId, bobCommand.city), ticketRepository.isFirst(mealType, BobStyleType.VEGETARIAN)).toString()
                }
                is ResponseAddMenu -> return "철이 없었죠 ${bobService.responseAddMenu(bobCommand.menu.name)} 메뉴 추가도 안해놨다는게.. 고마워요 호호호호호"
                is ResponseRemoveMenu -> return "${bobService.responseRemoveMenu(bobCommand.menu.name)} 메뉴가 제거되었어요옹"
                is ResponseAllMenu -> return MenuList(bobService.responseAllMenu()).toString()
                is ResponseStatistics -> return bobService.responseStatistics(userId, bobCommand.option).toString()
                is ResponseNotMe -> {
                    bobService.responseNotMe(userId)
                    return "밥 잘 챙겨 먹고 다녀요 니가 1그램이라도 사라지는건 싫으니까."
                }
                is ResponseChangeLivingCity -> {
                    return try {
                        val changedCityName = bobService.responseChangeLivingPlace(userId, bobCommand.city).city.cityName
                        "이제 $changedCityName 사시네요 홓홓"
                    } catch (e: UserAlreadyLivesThereException) {
                        "이미 ${bobCommand.city.name} 사람이에요"
                    }
                }
                is ResponseHelp -> {
//                    println("here")
                    return bobService.responseHelp()
                }
                is ResponseRecommendMenu -> return "오늘의 비스 추천 메뉴는 ${bobService.responseRecommendMenu(bobCommand.exceptingMenus.map{ it.name }).name}에요옹"
                is ResponseDetermineBobTeam -> {
                    val thisMealBobTeams = bobService.determineBobTeam(CityType.SEOUL, bobCommand.mealType)
                    thisMealBobTeams.toString().split("\n").forEach {
                        postMessageRequest(it)
                    }
                    return ""
                }
                null -> return ""
                else -> return ""
            }
        } catch (e: BobTicketAlreadyExistsException) {
            return "배고파요? 조금만 기다려줘요~ 흐하핳"
        } catch (e: ConstraintViolationException) {
            return "배고파요? 조금만 기다려줘요~ 흐하핳"
        } catch (e: DataIntegrityViolationException) {
            return "배고파요? 조금만 기다려줘요~ 흐하핳"
        } catch (e: NotTicketTimeException) {
            return "아직 밥 먹을 시간 아니에요 귀여운 꼬마 아가씨"
        } catch (e: NoSuchPlaceException) {
            return "그게 어디에요? 알려줘요~~"
        } catch (e: MenuAlreadyExistsException) {
            return "이미 있는 메뉴에요 이 바보~ ㅏ하하하하하핳"
        } catch (e: NoMenuSavedException) {
            return "저장된 메뉴가 한 개도 없어요~"
        } catch (e: NeverEatThisMonthException) {
            val user = userRepository.findByIdOrNull(userId)
            return "${user!!.name}님, 밥은 먹고 통계 확인하셔야죠 이 장난꾸러기! 하호호핳핳"
        } catch (e: NoSuchMenuException) {
            return "없는 메뉴 지우면 어떡해요 이 바보~ ㅏ하하하하하핳"
        } catch (e: NoBobTicketException) {
            return "으이그~ 밥 신청도 안 했으면서"
        } catch (e: InvalidCommandException) {
            return ""
        } catch (e: NotRegisteredUserException) {
            return "누구세요옹"
        } catch (e: NotEnoughBobTicketException) {
            return "서울은 오늘 밥팀이 없어요옹~ 밥들 챙겨 먹고 다녀요."
        } catch (e: NullPointerException) {
            return ""
        }
    }

    fun postMessageRequest(message: String): ResponseEntity<String.Companion> {
        val httpBody = JsonObject()
        println(SlackConstants.BOB_CHANNEL.content)
        httpBody.addProperty("channel", SlackConstants.BOB_CHANNEL.content)
        httpBody.addProperty("text", message)
        val httpHeaders = HttpHeaders()
        httpHeaders.add("Content-type", "application/json; charset=utf-8")
        httpHeaders.add("Authorization", "Bearer ${SlackConstants.BOT_TOKEN.content}")

        val httpEntity = HttpEntity(httpBody.toString(), httpHeaders)
        val restTemplate = RestTemplate()
        return restTemplate.exchange(
            "https://slack.com/api/chat.postMessage",
            HttpMethod.POST,
            httpEntity,
            String::class
        )
    }

    private fun getUserName(userCode: String): String {
        val httpHeaders = HttpHeaders()
        httpHeaders.add("Authorization", "Bearer ${SlackConstants.BOT_TOKEN.content}")
        val restTemplate = RestTemplate()
        val responseEntity = restTemplate.exchange<String>(
            "https://slack.com/api/users.info?user=$userCode",
            HttpMethod.GET,
            HttpEntity<String>(httpHeaders),
            String::class
        )
        val jsonObject = JsonParser.parseString(responseEntity.body).asJsonObject
        val user = jsonObject?.get("user") ?: throw NullPointerException()
        val uncleanedName = jsonObject.get("user").asJsonObject.get("profile").asJsonObject.get("display_name")
        println(uncleanedName.toString().trim('\"'))
        return uncleanedName.toString().trim('\"')
    }

    private fun isBot(userId: Long): Boolean {
        val user = userRepository.findByIdOrNull(userId) ?: return false
        return user.name.toLowerCase().contains("duckvis")
    }
}