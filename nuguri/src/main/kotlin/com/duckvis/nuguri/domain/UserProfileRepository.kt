package com.duckvis.nuguri.domain

import com.duckvis.nuguri.domain.UserProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository

@Repository
@EnableJpaRepositories
interface UserProfileRepository: JpaRepository<UserProfile, Long> {
}