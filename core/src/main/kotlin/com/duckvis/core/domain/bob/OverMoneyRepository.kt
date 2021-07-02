package com.duckvis.core.domain.bob

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OverMoneyRepository : JpaRepository<OverMoney, Long> {
  fun findByUserId(userId: Long): OverMoney?
  fun findAllByUserId(userId: Long): List<OverMoney>
}