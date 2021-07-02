package com.duckvis.bob.services

import com.duckvis.bob.dtos.covid.TicketCountDto
import com.duckvis.core.domain.bob.*
import com.duckvis.core.exceptions.bob.BobTeamAlreadyMatchedException
import com.duckvis.core.exceptions.bob.NeverMatchThemException
import com.duckvis.core.exceptions.bob.NotEnoughBobTicketException
import com.duckvis.core.types.bob.BobTimeType
import com.duckvis.core.types.bob.PayType
import com.duckvis.core.types.shared.CityType
import com.duckvis.core.utils.monthEndTime
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class BobTeamService(
  private val bobTicketQueryDslRepository: BobTicketQueryDslRepository,
  private val bobHistoryRepository: BobHistoryRepository,
  private val bobHistoryQueryDslRepository: BobHistoryQueryDslRepository,
) {

  companion object {
    const val BIG_BIG_BIG_NUMBER = 1000
    val log = LoggerFactory.getLogger(this.javaClass)
  }

  @Transactional
  fun determineBobTeam(
    city: CityType,
    bobTimeType: BobTimeType,
    decisionLogic: TeamDecisionLogic = CovidTeamDecisionLogic(),
    teamSortStrategy: TeamSortStrategy = FifoTeamSortStrategy // 코로나 이슈로 일시적으로 Fifo로 변경
  ): ThisMealBobTeamsList {
    markFirstTicket(bobTimeType)

    val tickets = bobTicketQueryDslRepository.findThisMealAllTickets(bobTimeType)

    // 코로나 이슈 -> allTickets 정렬 여기서
    val allTickets = sortTickets(tickets)

    checkBobTeamMatched(bobTimeType, allTickets.size, decisionLogic.minimumTeamCount)

    (0 until BIG_BIG_BIG_NUMBER).forEach { _ ->
      val thisMealBobTeamsList = teamMake(allTickets, bobTimeType, decisionLogic, teamSortStrategy)
      if (thisMealBobTeamsList.isAvailable) {
        thisMealBobTeamsList.teams.forEach { thisMealBobTeams -> archiveBobTeam(thisMealBobTeams) }
        return thisMealBobTeamsList
      }
    }
    throw NeverMatchThemException()
  }

  @Transactional
  fun sortTickets(tickets: List<BobTicket>): List<BobTicket> {
    if (tickets.size <= 2) {
      return tickets
    }

    val isMonthHalfOver = LocalDate.now().dayOfMonth > 15
    val from = if (isMonthHalfOver) {
      16
    } else {
      1
    }
    val to = if (isMonthHalfOver) {
      LocalDateTime.now().monthEndTime.dayOfMonth
    } else {
      15
    }
    val ordered = mutableListOf<BobTicket>()

    // bobTicket -> List<Int> : bobTeamNumber의 list
    val bobTeamNumbersMap = tickets.associateWith { ticket ->
      bobHistoryQueryDslRepository.bobTeamNumbers(
        ticket.userId,
        LocalDate.now().withDayOfMonth(from),
        LocalDate.now().withDayOfMonth(to)
      )
    }

    // bobTicket -> List<(otherTicket, Count)>
    val howMuchWithThisTickets = bobTeamNumbersMap.mapValues { (bobTicket, bobTeamNumbers) ->
      val otherEntries = bobTeamNumbersMap.filter { (otherTicket, _) ->
        otherTicket.id != bobTicket.id
      }
      otherEntries
        .map { (otherTicket, otherBobTeamNumbers) ->
          TicketCountDto(otherTicket, bobTeamNumbers.intersect(otherBobTeamNumbers).size)
        }
        .sortedByDescending { (otherTicket, count) -> count }
    }

    val maxEntry = howMuchWithThisTickets.maxByOrNull { (bobTicket, ticketCountDtos) ->
      log.info("sortedDtos: {}", ticketCountDtos)
      (ticketCountDtos.getOrNull(0)?.count ?: 0) + (ticketCountDtos.getOrNull(1)?.count ?: 0)
    }
      ?: return tickets.shuffled()

    log.info("maxEntry: {}", maxEntry)
    ordered.add(maxEntry.key)
    when (maxEntry.value.size) {
      0 -> return ordered + sortTickets(tickets)
      1 -> ordered.add(maxEntry.value[0].ticket)
      else -> ordered.addAll(maxEntry.value.subList(0, 2).map { ticketCountDtos -> ticketCountDtos.ticket })
    }

    log.info("ordered: {}", ordered.map { it.userId })

    return ordered + sortTickets(tickets - ordered)
  }

  @Transactional
  fun teamMake(
    allTickets: List<BobTicket>,
    bobTimeType: BobTimeType,
    decisionLogic: TeamDecisionLogic = CovidTeamDecisionLogic(),
    teamSortStrategy: TeamSortStrategy = RandomTeamSortStrategy
  ): ThisMealBobTeamsList {
    val supportTickets = bobTicketQueryDslRepository.findThisMealTicketsWithPayType(bobTimeType, PayType.SELECTSTAR)

    return when {
      allTickets.size < decisionLogic.minimumCount -> throw NotEnoughBobTicketException()
      (allTickets - supportTickets).size < decisionLogic.minimumTeamCount || supportTickets.size < decisionLogic.minimumTeamCount -> {
        val bobTeams = BobTeamMaker(allTickets, teamSortStrategy).make(decisionLogic, PayType.SELECTSTAR)
        ThisMealBobTeamsList(listOf(bobTeams))
      }
      else -> {
        val supportTeams = BobTeamMaker(supportTickets, teamSortStrategy).make(decisionLogic, PayType.SELECTSTAR)
        val selfTeams =
          BobTeamMaker(allTickets - supportTickets, teamSortStrategy).make(decisionLogic, PayType.MEMBER_SELF)
        ThisMealBobTeamsList(listOf(supportTeams, selfTeams))
      }
    }
  }

  private fun checkBobTeamMatched(bobTimeType: BobTimeType, allTicketNumber: Int, buffer: Int) {
    val matchedTicketNumber = bobHistoryQueryDslRepository.countThisMealHistory(bobTimeType).toInt()
    if (allTicketNumber != 0 && allTicketNumber - matchedTicketNumber < buffer) {
      throw BobTeamAlreadyMatchedException()
    }
  }

  private fun markFirstTicket(bobTimeType: BobTimeType) {
    bobTicketQueryDslRepository.findThisMealAllTickets(bobTimeType)
      .filter { it.isAnything }
      .minByOrNull { it.issuedOrderNumber }?.apply {
        this.setFirst()
      }
    bobTicketQueryDslRepository.findThisMealAllTickets(bobTimeType)
      .filter { it.isVegetarian }
      .minByOrNull { it.issuedOrderNumber }?.apply {
        this.setFirst()
      }
  }

  private fun createNewBobHistory(
    bobTicket: BobTicket,
    isBobLeader: Boolean = false,
    payType: PayType,
    bobTeamNumber: Int
  ): BobHistory {
    return bobHistoryRepository.save(BobHistory(bobTicket.id, bobTicket.userId, isBobLeader, bobTeamNumber, payType))
  }

  private fun archiveBobTeam(bobTeams: ThisMealBobTeams): List<BobHistory> {
    return bobTeamArchiver(bobTeams.vegetarianTeams + bobTeams.anythingTeams + bobTeams.lateEatTeams, bobTeams.payType)
  }

  private fun bobTeamArchiver(bobTeams: List<BobTeam>, payType: PayType): List<BobHistory> {
    val archived = mutableListOf<BobHistory>()
    val nowLastBobTeamNumber = getPresentMaxBobTeamNumber()
    bobTeams.withIndex().forEach { bobTeam ->
      bobTeam.value.memberTickets.forEach { ticket ->
        val bobHistory = createNewBobHistory(
          ticket, ticket == bobTeam.value.leaderTicket, payType,
          bobTeam.index + 1 + nowLastBobTeamNumber
        )
        val saved = bobHistoryRepository.save(bobHistory)
        archived.add(saved)
      }
    }
    return archived
  }

  private fun getPresentMaxBobTeamNumber(): Int {
    return bobHistoryRepository.findAll().maxByOrNull { it.bobTeamNumber }?.bobTeamNumber ?: 0
  }

}


//// TODO 테이블 수 != 도메인 객체 수
//// TODO 일급 컬렉션