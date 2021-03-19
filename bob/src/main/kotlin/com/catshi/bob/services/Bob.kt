package com.catshi.bob.services

import com.catshi.bob.domain.*
import com.catshi.bob.dtos.StatisticsDto
import com.catshi.bob.types.BobTimeType
import com.catshi.bob.types.StatisticsOption
import com.catshi.core.domain.User
import com.catshi.core.types.CityType
import com.catshi.core.types.UserPathType
import com.catshi.core.utils.TimeHandler
import java.time.LocalDateTime

interface Bob {
    fun responseMe(userId: Long, now: LocalDateTime = TimeHandler.nowDateTime()): BobTicket
    fun responseVegetarian(userId: Long, now: LocalDateTime = TimeHandler.nowDateTime()): BobTicket
    fun responseMeSpecificPlace(userId: Long, city: CityType, now: LocalDateTime = TimeHandler.nowDateTime()): BobTicket
    fun responseVegetarianSpecificPlace(userId: Long, city: CityType, now: LocalDateTime = TimeHandler.nowDateTime()): BobTicket
    fun responseNotMe(userId: Long, now: LocalDateTime = TimeHandler.nowDateTime())
//    fun responseIm(userCode: String, name: String, pathType: UserPathType = UserPathType.SLACK): User
    fun responseChangeLivingPlace(userId: Long, livingCity: CityType): User

    fun responseRecommendMenu(exceptedMenus: List<String>): Menu
    fun responseAllMenu(): List<Menu>
    fun responseAddMenu(name: String): Menu
    fun responseRemoveMenu(name: String)

    fun determineBobTeam(city: CityType, bobTimeType: BobTimeType,
                         decisionLogic: TeamDecisionLogic = CovidTeamDecisionLogic(),
                         teamSortStrategy: TeamSortStrategy = RandomTeamSortStrategy
    ): ThisMealBobTeams
    fun archiveBobTeam(bobTeams: ThisMealBobTeams): List<BobHistory>
    fun responseHelp(): String
    fun responseStatistics(userId: Long, option: StatisticsOption): StatisticsDto
}