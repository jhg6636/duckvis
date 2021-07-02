package com.duckvis.core.domain.nuguri

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SubProjectRepository : JpaRepository<SubProject, Long> {

  fun existsByNameOrNickname(name: String, nickname: String): Boolean
  fun findByNameAndProjectId(name: String, projectId: Long): SubProject?
  fun findByNicknameAndProjectId(nickname: String, projectId: Long): SubProject?
  fun findAllByProjectId(projectId: Long): List<SubProject>

}