package com.duckvis.core.domain.nuguri

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository

@Repository
@EnableJpaRepositories
interface ProjectRepository : JpaRepository<Project, Long> {
  fun existsByName(name: String): Boolean
  fun existsByNickname(nickname: String): Boolean
  fun findByNameOrNickname(name: String, nickname: String): Project?
}