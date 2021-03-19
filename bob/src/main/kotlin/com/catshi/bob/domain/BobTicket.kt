package com.catshi.bob.domain

import com.catshi.bob.types.BobStyleType
import com.catshi.bob.types.BobTimeType
import com.catshi.bob.types.IssuedOrderType
import com.catshi.core.domain.User
import com.catshi.core.types.CityType
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalTime
import javax.persistence.*

@Entity
@EntityScan(basePackages = ["com.catshi.core"])
@Table(uniqueConstraints = [ UniqueConstraint(columnNames = ["user_id", "date", "bob_time_type"]) ])
class BobTicket(
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(nullable = false) val user: User,
    val date: LocalDate,
    val time: LocalTime,
    @Enumerated(EnumType.STRING) var bobTimeType: BobTimeType,
    @Enumerated(EnumType.STRING) var bobStyle: BobStyleType,
    @Enumerated(EnumType.STRING) var city: CityType,
    @Enumerated(EnumType.STRING) var issuedOrder: IssuedOrderType = IssuedOrderType.NOT_FIRST
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = -1

    fun changeCity(city: CityType) {
        this.city = city
    }

    fun isSameCity(city: CityType): Boolean {
        return this.city == city
    }

    val isVegetarian: Boolean
        get() = this.bobStyle == BobStyleType.VEGETARIAN

    val isAnything: Boolean
        get() = this.bobStyle == BobStyleType.ANYTHING


    fun changeBobStyleType(styleType: BobStyleType) {
        this.bobStyle = styleType
    }

    fun isSameStyle(styleType: BobStyleType): Boolean {
        return this.bobStyle == styleType
    }

    val isFirst: Boolean
        get() = this.issuedOrder == IssuedOrderType.FIRST

    fun setFirst() {
        this.issuedOrder = IssuedOrderType.FIRST
    }

    fun tagString(): String {
        return "<@${this.user.userCode}>"
    }
}