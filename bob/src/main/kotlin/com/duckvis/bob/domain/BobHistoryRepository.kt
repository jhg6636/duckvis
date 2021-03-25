package com.duckvis.bob.domain

import com.duckvis.bob.domain.BobHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository

@Repository
@EnableJpaRepositories
interface BobHistoryRepository : JpaRepository<BobHistory, Long>