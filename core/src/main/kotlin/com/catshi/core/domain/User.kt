package com.catshi.core.domain

import com.catshi.core.exceptions.UserAlreadyExistsException
import com.catshi.core.exceptions.UserAlreadyLivesThereException
import com.catshi.core.types.CityType
import com.catshi.core.types.UserLevelType
import com.catshi.core.types.UserPathType
import org.springframework.context.annotation.ComponentScan
import javax.persistence.*

@Entity
@ComponentScan(basePackages = ["com.catshi"])
class User(
    val userCode: String,
    var name: String,
    @Enumerated(EnumType.STRING) var city: CityType = CityType.SEOUL,
    @Enumerated(EnumType.STRING) private val path: UserPathType = UserPathType.SLACK,
    @Enumerated(EnumType.STRING) var userLevel: UserLevelType = UserLevelType.NORMAL,
    var teamId: Long? = null,
    var multiTeam: String = "", // 팀이 여러 개일 경우, 메인 팀은 team에 저장, 모든 팀 Id를 String화 하여 multiTeam에 저장한다.
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1

    fun changeCity(city: CityType) {
        if (this.city == city) throw UserAlreadyLivesThereException()
        this.city = city
    }

    fun setTeam(teamId: Long) {
        if (this.teamId == null) {
            this.teamId = teamId
        } else if (multiTeam == "") {
            this.multiTeam = "${this.teamId},${teamId}"
        } else {
            this.multiTeam += ",$teamId"
        }
    }

    fun exitTeam(teamId: Long) {
        if (this.multiTeam != "") {
            val teamIdList = this.multiTeam.split(",")
            val afterDeleted = teamIdList.filter { it.toLong() != teamId }
            if (afterDeleted.size == 1) {
                this.teamId = afterDeleted.first().toLong()
                this.multiTeam = ""
            } else {
                this.multiTeam = afterDeleted.joinToString(",")
            }
        } else {
            this.teamId = null
        }
    }
}