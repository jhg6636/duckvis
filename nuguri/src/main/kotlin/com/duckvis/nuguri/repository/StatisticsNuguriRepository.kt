package com.duckvis.nuguri.repository

import com.duckvis.core.domain.nuguri.AttendanceCard
import com.duckvis.core.domain.nuguri.Project
import com.duckvis.core.domain.nuguri.QAttendanceCard.attendanceCard
import com.duckvis.core.domain.nuguri.QUserTeam.userTeam
import com.duckvis.core.domain.nuguri.SubProject
import com.duckvis.core.domain.shared.QUser.user
import com.duckvis.core.domain.shared.User
import com.duckvis.core.dtos.nuguri.service.params.v2.domain.statistics.NuguriStatisticsRequestParameterDto
import com.duckvis.core.types.shared.UserLevelType
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class StatisticsNuguriRepository(
  private val queryFactory: JPAQueryFactory,
) {

  fun getAllCheckableCards(
    params: NuguriStatisticsRequestParameterDto,
    admin: User,
    project: Project?,
    subProject: SubProject?,
    memberIds: List<Long>,
    from: LocalDateTime,
    to: LocalDateTime,
  ): List<AttendanceCard> {
    val userIds = checkableUserIds(params, admin)

    val projectQuery = if (project != null) {
      attendanceCard.projectId.eq(project.id)
    } else {
      attendanceCard.isNotNull
    }
    val subProjectQuery = if (subProject != null) {
      attendanceCard.subProjectId.eq(subProject.id)
    } else {
      attendanceCard.isNotNull
    }
    val memberQuery = if (memberIds.isNotEmpty()) {
      attendanceCard.userId.`in`(memberIds)
    } else {
      attendanceCard.isNotNull
    }

    val whereQuery = attendanceCard.userId.`in`(userIds)
      .and(attendanceCard.isExtended.eq(params.workTypeDto.isExtended))
      .and(attendanceCard.isHoliday.eq(params.workTypeDto.isHoliday))
      .and(attendanceCard.isNight.eq(params.workTypeDto.isNight))
      .and(projectQuery)
      .and(subProjectQuery)
      .and(memberQuery)
      .and(attendanceCard.loginDateTime.between(from, to))

    return queryFactory
      .select(attendanceCard)
      .from(attendanceCard)
      .where(
        whereQuery
      )
      .fetch()
  }

  fun checkableUserIds(params: NuguriStatisticsRequestParameterDto, admin: User): List<Long> {
    if (!params.isAdmin) {
      return listOf()
    }
    val whereQuery = when {
      admin.isAdmin -> user.level.ne(UserLevelType.EXIT)
      params.userTeam?.isManager == true -> {
        if (params.userTeam == null) {
          return listOf()
        }
        userTeam.teamId.eq(params.userTeam!!.teamId)
      }
      else -> return listOf()
    }
    return queryFactory
      .select(user.id)
      .from(user)
      .leftJoin(userTeam)
      .on(
        userTeam.userId.eq(user.id)
      )
      .where(
        whereQuery
      )
      .fetch()
  }

}