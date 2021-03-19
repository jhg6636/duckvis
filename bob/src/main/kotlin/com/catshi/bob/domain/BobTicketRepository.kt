package com.catshi.bob.domain

import com.catshi.bob.types.BobStyleType
import com.catshi.bob.types.BobTimeType
import com.catshi.core.types.CityType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.ZoneId

@Repository
@EnableJpaRepositories
interface BobTicketRepository : JpaRepository<BobTicket, Long> {
//    @Transactional
////    fun findByUserIdAndBobTeamNumberIsNull(userId: Long): BobTicket?
//    fun findByUserIdAndBobTimeTypeAndDate(
//        userId: Long, bobTimeType: BobTimeType,
//        date: LocalDate = LocalDate.now(ZoneId.of("Asia/Seoul"))
//    ): BobTicket?
//
//    @Transactional
////    fun findAllByCityAndBobTeamNumberIsNull(cityType: CityType): List<BobTicket>
//    fun findAllByCityAndBobTimeTypeAndDate(
//        cityType: CityType, bobTimeType: BobTimeType,
//        date: LocalDate = LocalDate.now(ZoneId.of("Asia/Seoul"))
//    ): List<BobTicket>
//
//    @Transactional
////    fun countByCityAndBobStyleAndBobTeamNumberIsNull(cityType: CityType, styleType: BobStyleType): Int
//    fun countByCityAndBobStyleAndBobTimeTypeAndDate(
//        cityType: CityType, styleType: BobStyleType, bobTimeType: BobTimeType,
//        date: LocalDate = LocalDate.now(ZoneId.of("Asia/Seoul"))
//    ): Int
//
//    @Transactional
////    fun findAllByCityAndBobStyleAndBobTeamNumberIsNull(cityType: CityType, styleType: BobStyleType): List<BobTicket>
//    fun findAllByCityAndBobStyleAndBobTimeTypeAndDate(
//        cityType: CityType, styleType: BobStyleType, bobTimeType: BobTimeType,
//        date: LocalDate = LocalDate.now(ZoneId.of("Asia/Seoul"))
//    ): List<BobTicket>
//
//    @Transactional
////    fun findFirstByBobTeamNumberIsNullOrderByTimeAsc(): BobTicket?
//    fun findFirstByBobTimeTypeAndDateOrderByTimeAsc(
//        bobTimeType: BobTimeType,
//        date: LocalDate = LocalDate.now(ZoneId.of("Asia/Seoul"))
//    ): BobTicket?
}