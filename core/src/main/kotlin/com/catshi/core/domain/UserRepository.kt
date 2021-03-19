package com.catshi.core.domain

import com.catshi.core.types.UserPathType
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@EnableJpaRepositories
interface UserRepository : JpaRepository<User, Long> {
    @Transactional
    fun findByUserCodeAndPath(stringId: String, pathType: UserPathType): User?

    fun countByTeamId(teamId: Long): Int
}