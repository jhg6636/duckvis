package com.duckvis.core.domain

import com.duckvis.core.exceptions.UserAlreadyLivesThereException
import com.duckvis.core.types.CityType
import com.duckvis.core.types.UserLevelType
import com.duckvis.core.types.UserPathType
import org.springframework.context.annotation.ComponentScan
import javax.persistence.*

@Entity
@ComponentScan(basePackages = ["com.catshi"])
class User(
    val code: String,
    var name: String,
    @Enumerated(EnumType.STRING) var city: CityType = CityType.SEOUL,
    @Enumerated(EnumType.STRING) private val path: UserPathType = UserPathType.SLACK,
    @Enumerated(EnumType.STRING) var level: UserLevelType = UserLevelType.NORMAL,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1

    fun changeCity(city: CityType) {
        if (this.city == city) throw UserAlreadyLivesThereException()
        this.city = city
    }

    private val isManager: Boolean
        get() = this.level == UserLevelType.TEAM_MANAGER

    private val isAdmin: Boolean
        get() = this.level == UserLevelType.ADMIN
}