package com.duckvis.nuguri.repository

import com.duckvis.core.domain.nuguri.QSubProject.subProject
import com.duckvis.core.domain.nuguri.SubProject
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component


// THINKING Domain Service의 도입을 고민
@Component
class SubProjectNuguriRepository(
  private val queryFactory: JPAQueryFactory,
) {

  fun getSubProject(nameOrNickname: String?, projectId: Long?): SubProject? {
    if (nameOrNickname == null) {
      return null
    }
    if (projectId == null) {
      throw NuguriException(ExceptionType.SUB_PROJECT_HAS_NO_PROJECT)
    }
    return queryFactory
      .select(subProject)
      .from(subProject)
      .where(
        subProject.projectId.eq(projectId),
        subProject.name.eq(nameOrNickname).or(subProject.nickname.eq(nameOrNickname))
      )
      .fetchOne()
      ?: throw NuguriException(ExceptionType.NO_SUCH_SUB_PROJECT)
  }

  fun isAlreadyExisting(name: String, nickname: String, projectId: Long): Boolean {
    return queryFactory
      .select(subProject)
      .from(subProject)
      .where(
        subProject.projectId.eq(projectId),
        subProject.name.eq(name).or(subProject.nickname.eq(nickname))
      )
      .fetch()
      .isNotEmpty()
  }

}