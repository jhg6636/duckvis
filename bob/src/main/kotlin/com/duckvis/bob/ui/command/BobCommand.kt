package com.duckvis.bob.ui.command

import com.duckvis.bob.domain.Menu
import com.duckvis.bob.types.BobTimeType
import com.duckvis.bob.types.StatisticsOption
import com.duckvis.core.types.CityType
import org.springframework.stereotype.Component

@Component
interface BobCommand
class ResponseMe : BobCommand
class ResponseVegetarian : BobCommand
class ResponseMeSpecificPlace(val city: CityType) : BobCommand
class ResponseVegetarianSpecificPlace(val city: CityType) : BobCommand
class ResponseNotMe : BobCommand
class ResponseChangeLivingCity(val city: CityType) : BobCommand
class ResponseStatistics(val option: StatisticsOption) : BobCommand
class ResponseAddMenu(val menu: Menu) : BobCommand
class ResponseRemoveMenu(val menu: Menu) : BobCommand
class ResponseRecommendMenu(val exceptingMenus: List<Menu>) : BobCommand
class ResponseAllMenu : BobCommand
class ResponseHelp : BobCommand
class ResponseDetermineBobTeam(val mealType: BobTimeType) : BobCommand
