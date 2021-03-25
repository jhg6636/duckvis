package com.duckvis.nuguri.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository

@Repository
@EnableJpaRepositories
interface HolidayRepository: JpaRepository<Holiday, Long> {
}