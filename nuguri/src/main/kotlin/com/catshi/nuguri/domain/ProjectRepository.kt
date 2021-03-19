package com.catshi.nuguri.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjectRepository : JpaRepository<Project, Long> {
    fun existsByName(name: String): Boolean
    fun existsByNickname(nickname: String): Boolean
}