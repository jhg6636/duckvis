package com.duckvis.bob.ui.command

import com.duckvis.core.domain.bob.Menu
import com.duckvis.core.types.bob.BobTimeType
import com.duckvis.core.types.bob.StatisticsOption
import com.duckvis.core.types.shared.CityType
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
interface BobCommand
class ResponseMe : BobCommand
class ResponseVegetarian : BobCommand
class ResponseLateEat : BobCommand
class ResponseMeSpecificPlace(val city: CityType) : BobCommand
class ResponseVegetarianSpecificPlace(val city: CityType) : BobCommand
class ResponseLateEatSpecificPlace(val city: CityType) : BobCommand
class ResponseNotMe : BobCommand
class ResponseChangeLivingCity(val city: CityType) : BobCommand
class ResponseStatistics(val option: StatisticsOption) : BobCommand
class ResponseAddMenu(val menu: Menu) : BobCommand
class ResponseRemoveMenu(val menu: Menu) : BobCommand
class ResponseRecommendMenu(val exceptingMenus: List<Menu>) : BobCommand
class ResponseAllMenu : BobCommand
class ResponseHelp : BobCommand
class ResponseDetermineBobTeam(val mealType: BobTimeType) : BobCommand
class ResponsePayWithSupport(val money: Int) : BobCommand
class ResponsePayWithoutSupport(val money: Int) : BobCommand
class ResponseSelfPayWithSupport(val deliveryCost: Int) : BobCommand
class ResponseSelfPayWithoutSupport(val deliveryCost: Int) : BobCommand
class ResponseModifyPayWithSupport(val money: Int, val date: LocalDate, val bobTimeType: BobTimeType) : BobCommand
class ResponseModifyPayWithoutSupport(val money: Int, val date: LocalDate, val bobTimeType: BobTimeType) : BobCommand
class ResponseModifySelfPayWithSupport(val deliveryCost: Int, val date: LocalDate, val bobTimeType: BobTimeType) :
  BobCommand

class ResponseModifySelfPayWithoutSupport(val deliveryCost: Int, val date: LocalDate, val bobTimeType: BobTimeType) :
  BobCommand

class ResponseTotalOverMoney : BobCommand
class ResponseShowAllPays(val userName: String, val size: Int) : BobCommand
