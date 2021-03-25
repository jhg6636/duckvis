package com.duckvis.nuguri.domain

import com.duckvis.nuguri.domain.Project
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjectRepository : JpaRepository<Project, Long> {
    fun existsByName(name: String): Boolean
    fun existsByNickname(nickname: String): Boolean
}