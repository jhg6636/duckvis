package com.duckvis.core.domain.bob

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserPayRepository : JpaRepository<UserPay, Long> {
  fun findByUserId(userId: Long): UserPay?
}