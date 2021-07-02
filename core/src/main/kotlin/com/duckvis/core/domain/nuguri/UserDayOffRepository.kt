package com.duckvis.core.domain.nuguri

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserDayOffRepository: JpaRepository<UserDayOff, Long> {
}