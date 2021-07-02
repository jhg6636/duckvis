package com.duckvis.nuguri.repository

import com.duckvis.core.domain.nuguri.Project
import com.duckvis.core.domain.nuguri.QProject.project
import com.duckvis.core.exceptions.nuguri.ExceptionType
import com.duckvis.core.exceptions.nuguri.NuguriException
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component

@Component
class ProjectNuguriRepository(
  private val queryFactory: JPAQueryFactory
) {

  fun getProject(nameOrNickname: String?): Project? {
    if (nameOrNickname == null) {
      return null
    }
    return queryFactory
      .select(project)
      .from(project)
      .where(
        project.name.eq(nameOrNickname).or(project.nickname.eq(nameOrNickname))
      )
      .fetchOne()
      ?: throw NuguriException(ExceptionType.NO_SUCH_PROJECT)
  }

}