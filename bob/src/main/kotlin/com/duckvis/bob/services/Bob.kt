package com.duckvis.bob.services

import com.duckvis.bob.domain.BobHistory
import com.duckvis.bob.domain.Menu
import com.duckvis.bob.domain.ThisMealBobTeams
import com.duckvis.bob.dtos.StatisticsDto
import com.duckvis.bob.types.BobTimeType
import com.duckvis.bob.types.StatisticsOption
import com.duckvis.core.domain.User
import com.duckvis.core.types.CityType
import com.duckvis.core.utils.TimeHandler
import com.duckvis.bob.domain.BobTicket
import java.time.LocalDateTime

interface Bob {
    fun responseMe(userId: Long, now: LocalDateTime = TimeHandler.nowDateTime()): BobTicket
    fun responseVegetarian(userId: Long, now: LocalDateTime = TimeHandler.nowDateTime()): BobTicket
    fun responseMeSpecificPlace(userId: Long, city: CityType, now: LocalDateTime = TimeHandler.nowDateTime()): BobTicket
    fun responseVegetarianSpecificPlace(
        userId: Long,
        city: CityType,
        now: LocalDateTime = TimeHandler.nowDateTime()
    ): BobTicket

    fun responseNotMe(userId: Long, now: LocalDateTime = TimeHandler.nowDateTime())

    //    fun responseIm(userCode: String, name: String, pathType: UserPathType = UserPathType.SLACK): User
    fun responseChangeLivingPlace(userId: Long, livingCity: CityType): User

    fun responseRecommendMenu(exceptedMenus: List<String>): Menu
    fun responseAllMenu(): List<Menu>
    fun responseAddMenu(name: String): Menu
    fun responseRemoveMenu(name: String)

    fun determineBobTeam(
        city: CityType, bobTimeType: BobTimeType,
        decisionLogic: TeamDecisionLogic = CovidTeamDecisionLogic(),
        teamSortStrategy: TeamSortStrategy = RandomTeamSortStrategy
    ): ThisMealBobTeams

    fun archiveBobTeam(bobTeams: ThisMealBobTeams): List<BobHistory>
    fun responseHelp(): String
    fun responseStatistics(userId: Long, option: StatisticsOption): StatisticsDto
}