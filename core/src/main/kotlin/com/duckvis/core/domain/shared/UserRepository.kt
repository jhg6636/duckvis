package com.duckvis.core.domain.shared

import com.duckvis.core.types.shared.UserPathType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository

@Repository
@EnableJpaRepositories
interface UserRepository : JpaRepository<User, Long> {
  fun findByCodeAndPath(stringId: String, pathType: UserPathType): User?
  fun findByName(name: String): User?
}