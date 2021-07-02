package com.duckvis.core.domain.bob

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MenuRepository : JpaRepository<Menu, Long> {
  fun existsByName(name: String): Boolean
  fun findByName(name: String): Menu?
}