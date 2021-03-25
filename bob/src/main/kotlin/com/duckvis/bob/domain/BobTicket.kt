package com.duckvis.bob.domain

import com.duckvis.bob.types.BobStyleType
import com.duckvis.bob.types.BobTimeType
import com.duckvis.bob.types.IssuedOrderType
import com.duckvis.core.domain.User
import com.duckvis.core.types.CityType
import org.springframework.boot.autoconfigure.domain.EntityScan
import java.time.LocalDate
import java.time.LocalTime
import javax.persistence.*

@Entity
@EntityScan(basePackages = ["com.duckvis.core"])
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "date", "bobTimeType"])])
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
        return "<@${this.user.code}>"
    }
}