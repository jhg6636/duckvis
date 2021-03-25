package com.duckvis.nuguri.domain

import com.duckvis.nuguri.types.Gender
import java.time.LocalDate
import javax.persistence.*

@Entity
class UserProfile(
    private val userId: Long,
    private var name: String,
    @Enumerated(EnumType.STRING) private val gender: Gender,
    private val birthDate: LocalDate,
    private val joinDate: LocalDate,
    private var statusMessage: String,
    private var targetWorkHour: Int,
    private var discountFromVacation: Boolean = true,
    ) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = -1

    fun changeName(newName: String) {
        this.name = newName
    }

    fun changeStatusMessage(message: String) {
        this.statusMessage = message
    }

    fun setTargetWorkHour(target: Int) {
        this.targetWorkHour = target
    }

    fun changeDiscountOption(fromVacation: Boolean) {
        this.discountFromVacation = fromVacation
    }
}