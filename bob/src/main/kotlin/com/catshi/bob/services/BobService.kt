package com.catshi.bob.services

import com.catshi.bob.domain.*
import com.catshi.bob.dtos.StatisticsDto
import com.catshi.bob.exceptions.*
import com.catshi.bob.types.BobStyleType
import com.catshi.bob.types.BobTimeType
import com.catshi.bob.types.StatisticsOption
import com.catshi.core.domain.User
import com.catshi.core.domain.UserRepository
import com.catshi.core.types.CityType
import com.catshi.core.types.UserPathType
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class BobService(
    private val bobTicketRepository: BobTicketRepository,
    private val bobTicketQueryDslRepository: BobTicketQueryDslRepository,
    private val userRepository: UserRepository,
    private val bobHistoryRepository: BobHistoryRepository,
    private val bobHistoryQueryDslRepository: BobHistoryQueryDslRepository,
    private val menuRepository: MenuRepository,
) : Bob {

    @Transactional
    override fun responseMe(userId: Long, now: LocalDateTime): BobTicket {
        val user = userRepository.findByIdOrNull(userId) ?: throw NotRegisteredUserException()
        return saveTicket(userId, BobStyleType.ANYTHING, user.city, now)
    }

    @Transactional
    override fun responseVegetarian(userId: Long, now: LocalDateTime): BobTicket {
        val user = userRepository.findByIdOrNull(userId) ?: throw NotRegisteredUserException()
        return saveTicket(userId, BobStyleType.VEGETARIAN, user.city, now)
    }

    @Transactional
    override fun responseMeSpecificPlace(userId: Long, city: CityType, now: LocalDateTime): BobTicket {
        return saveTicket(userId, BobStyleType.ANYTHING, city, now)
    }

    @Transactional
    override fun responseVegetarianSpecificPlace(userId: Long, city: CityType, now: LocalDateTime): BobTicket {
        return saveTicket(userId, BobStyleType.VEGETARIAN, city, now)
    }

    private fun saveTicket(userId: Long, styleType: BobStyleType, city: CityType, now: LocalDateTime): BobTicket {
        val user = this.userRepository.findByIdOrNull(userId) ?: throw NotRegisteredUserException()
        val thisMealTicket = bobTicketQueryDslRepository.getThisMealTicket(userId, BobTimeType.of(now))
            ?: return createBobTicket(user, styleType, city, now)

        if (thisMealTicket.isSameCity(city) && thisMealTicket.isSameStyle(styleType)) throw BobTicketAlreadyExistsException()

        thisMealTicket.changeCity(city)
        thisMealTicket.changeBobStyleType(styleType)
        return thisMealTicket
    }

    @Transactional
    override fun responseNotMe(userId: Long, now: LocalDateTime) {
        bobTicketQueryDslRepository.getThisMealTicket(userId, BobTimeType.of(now))
            ?.let {
                bobTicketRepository.delete(it)
            }
            ?: throw NoBobTicketException()
    }

//    @Transactional
//    override fun responseIm(userCode: String, name: String, pathType: UserPathType): User {
//        val user = userRepository.findByUserCodeAndPath(userCode, pathType)
//            ?: return createNewUser(userCode, name, CityType.SEOUL, pathType)
//        user.checkSameName(name)
//        user.changeName(name)
//        return user
//    }

//    private fun createNewUser(userCode: String, name: String, city: CityType, pathType: UserPathType): User {
//        return User(userCode, name, city, pathType).let { this.userRepository.save(it) }
//    }


    @Transactional
    override fun responseChangeLivingPlace(userId: Long, city: CityType): User {
        val user = userRepository.findByIdOrNull(userId)
            ?: throw NotRegisteredUserException()
        user.changeCity(city)
        return user
    }

    @Transactional
    override fun responseRecommendMenu(exceptedMenus: List<String>): Menu {
        if (menuRepository.count() == 0L) throw NoMenuSavedException()
        var recommendation = pickOneMenu()
        while (exceptedMenus.contains(recommendation.name)) recommendation = pickOneMenu()
        return recommendation
    }

    private fun pickOneMenu(): Menu {
        return menuRepository.findAll().shuffled()[0]
    }

    @Transactional
    override fun responseAddMenu(name: String): Menu {
        if (menuRepository.existsByName(name)) throw MenuAlreadyExistsException()
        return menuRepository.save(Menu(name))
    }

    @Transactional
    override fun determineBobTeam(
        city: CityType, bobTimeType: BobTimeType,
        decisionLogic: TeamDecisionLogic, teamSortStrategy: TeamSortStrategy
    ): ThisMealBobTeams {
        markFirstTicket(bobTimeType)
//        leaveFirstTicketsOnly(bobTimeType)
        val allTickets = bobTicketQueryDslRepository.findThisMealAllTickets(bobTimeType)
        checkBobTeamMatched(bobTimeType, allTickets.size)

        return BobTeamMaker(allTickets, teamSortStrategy).make(decisionLogic)
    }

    @Transactional
    fun leaveFirstTicketsOnly(bobTimeType: BobTimeType) {
        val allTickets = bobTicketQueryDslRepository.findThisMealAllTickets(bobTimeType)
        val allUsers = allTickets.map { it.user }.distinct()

        if (allTickets.size != allUsers.size) {
            allUsers
                .filter {
                    bobTicketQueryDslRepository.hasMoreThanOneTicket(it.id, bobTimeType)
                }
                .forEach {
                    val usersTicket = allTickets.filter { ticket -> ticket.user.id == it.id }
                    usersTicket.sortedBy { ticket -> ticket.time }
                        .subList(1, usersTicket.size)
                        .forEach { ticket -> bobTicketRepository.delete(ticket) }
                }
        }
    }

    private fun checkBobTeamMatched(bobTimeType: BobTimeType, waitingTicketNumber: Int) {
        val matchedTicketNumber = bobHistoryQueryDslRepository.countThisMealHistory(bobTimeType).toInt()
        if (waitingTicketNumber != 0 && matchedTicketNumber == waitingTicketNumber) {
            throw BobTeamAlreadyMatchedException()
        }
    }

    private fun markFirstTicket(bobTimeType: BobTimeType) {
        bobTicketQueryDslRepository.findThisMealAllTickets(bobTimeType)
            .filter { it.isAnything }
            .minByOrNull { it.time }?.apply {
                this.setFirst()
            }
        bobTicketQueryDslRepository.findThisMealAllTickets(bobTimeType)
            .filter { it.isVegetarian }
            .minByOrNull { it.time }?.apply {
                this.setFirst()
            }
    }

    private fun createNewBobHistory(
        bobTicket: BobTicket,
        isBobLeader: Boolean = false,
        bobTeamNumber: Int
    ): BobHistory {
        return bobHistoryRepository.save(BobHistory(bobTicket, isBobLeader, bobTeamNumber))
    }

    private fun createBobTicket(user: User, bobStyle: BobStyleType, bobCity: CityType, now: LocalDateTime): BobTicket {
        return bobTicketRepository.save(
            BobTicket(
                user,
                now.toLocalDate(),
                now.toLocalTime(),
                BobTimeType.of(now),
                bobStyle,
                bobCity
            )
        )
    }

    @Transactional
    override fun archiveBobTeam(bobTeams: ThisMealBobTeams): List<BobHistory> {
        return bobTeamArchiver(bobTeams.vegetarianTeams + bobTeams.anythingTeams)
    }

    private fun bobTeamArchiver(bobTeams: List<BobTeam>): List<BobHistory> {
        val archived = mutableListOf<BobHistory>()
        val nowLastBobTeamNumber = getPresentMaxBobTeamNumber()
        bobTeams.withIndex().forEach { bobTeam ->
            bobTeam.value.memberTickets.forEach { ticket ->
                createNewBobHistory(
                    ticket, ticket == bobTeam.value.leaderTicket,
                    bobTeam.index + 1 + nowLastBobTeamNumber
                ).apply {
                    bobHistoryRepository.save(this)
                    archived.add(this)
                }
            }
        }
        return archived
    }

    private fun getPresentMaxBobTeamNumber(): Int {
        return bobHistoryRepository.findAll().maxByOrNull { it.bobTeamNumber }?.bobTeamNumber ?: 0
    }

    override fun responseHelp(): String {
        return "(기존기능 유지)\n밥 투표 : 저요, ㅈㅇ\n투표 취소 : 안먹, ㅇㅁ\n기본 지역 설정 (서울, 대전 - 기본값은 서울입니다) : 이제 @@ 살래\n" +
                "오늘만 다른 지역에서 먹고 싶을 때 : 서울에서, 대전에서\n채식 투표 : ㅊㅅ, 채식\n오늘만 다른 지역에서 채식 : ㄷㅈㅊㅅ, ㅅㅇㅊㅅ, 대전채식, 서울채식\n" +
                "\n(신규기능!)\n메뉴추천: ㅊㅊ, 메뉴추천 (특정 메뉴 제외: %제외 오이,피클,가지 / %제외 오이)\n전체메뉴: !메뉴\n" +
                "메뉴추가: !메뉴추가 돼지국밥\n메뉴제거: !메뉴제거 돼지국밥\n" +
                "밥통계, ㅂㅌㄱ (%밥부장, %짝꿍): 써보세요"
    }

    @Transactional
    override fun responseStatistics(userId: Long, option: StatisticsOption): StatisticsDto {
        val bobLeaderCount = bobHistoryQueryDslRepository.bobLeaderCount(userId)
        val teammates = bobHistoryQueryDslRepository.teammates(userId)
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

    @Transactional
    override fun responseAllMenu(): List<Menu> {
        return menuRepository.findAll()
    }

    @Transactional
    override fun responseRemoveMenu(name: String) {
        menuRepository.findByName(name)
            ?.apply { menuRepository.delete(this) }
            ?: throw NoSuchMenuException()
    }
}
