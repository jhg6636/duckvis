package com.duckvis.core.domain.nuguri

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
@EnableJpaRepositories
interface HolidayRepository : JpaRepository<Holiday, Long> {
  fun existsByDateEquals(date: LocalDate): Boolean
  fun findByDateEquals(date: LocalDate): Holiday?
}