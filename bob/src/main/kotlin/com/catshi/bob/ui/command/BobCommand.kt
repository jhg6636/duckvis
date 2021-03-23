package com.catshi.bob.ui.command

import com.catshi.bob.domain.Menu
import com.catshi.bob.types.BobTimeType
import com.catshi.bob.types.StatisticsOption
import com.catshi.core.types.CityType
import org.springframework.stereotype.Component

@Component
interface BobCommand
class ResponseMe(): BobCommand
class ResponseVegetarian(): BobCommand
class ResponseMeSpecificPlace(val city: CityType): BobCommand
class ResponseVegetarianSpecificPlace(val city: CityType): BobCommand
class ResponseNotMe(): BobCommand
class ResponseChangeLivingCity(val city: CityType): BobCommand
class ResponseStatistics(val option: StatisticsOption): BobCommand
class ResponseAddMenu(val menu: Menu): BobCommand
class ResponseRemoveMenu(val menu: Menu): BobCommand
class ResponseRecommendMenu(val exceptingMenus: List<Menu>): BobCommand
class ResponseAllMenu(): BobCommand
class ResponseHelp(): BobCommand
class ResponseDetermineBobTeam(val mealType: BobTimeType): BobCommand
