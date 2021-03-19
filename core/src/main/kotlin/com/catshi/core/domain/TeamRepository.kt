package com.catshi.core.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository

@Repository
@EnableJpaRepositories
interface TeamRepository : JpaRepository<Team, Long> {
    fun existsByName(name: String): Boolean
    fun findByName(name: String): Team?
}