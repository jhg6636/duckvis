package com.duckvis.core.domain.nuguri

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository

@Repository
@EnableJpaRepositories
interface UserProfileRepository : JpaRepository<UserProfile, Long> {
  fun findByUserId(userProfile: Long): UserProfile?
}