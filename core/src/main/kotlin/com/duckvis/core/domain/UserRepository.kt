package com.duckvis.core.domain

import com.duckvis.core.types.UserPathType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@EnableJpaRepositories
interface UserRepository : JpaRepository<User, Long> {
    @Transactional
    fun findByCodeAndPath(stringId: String, pathType: UserPathType): User?
}