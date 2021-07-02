package com.duckvis.bob.services

import com.duckvis.core.domain.bob.Menu
import com.duckvis.core.domain.bob.MenuRepository
import com.duckvis.core.exceptions.bob.MenuAlreadyExistsException
import com.duckvis.core.exceptions.bob.NoMenuSavedException
import com.duckvis.core.exceptions.bob.NoSuchMenuException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BobMenuService(
  private val menuRepository: MenuRepository,
) {
  @Transactional
  fun responseRecommendMenu(exceptedMenus: List<String>): Menu {
    if (menuRepository.count() == 0L) throw NoMenuSavedException()
    var recommendation = pickOneMenu()
    while (exceptedMenus.contains(recommendation.name)) recommendation = pickOneMenu()
    return recommendation
  }

  private fun pickOneMenu(): Menu {
    return menuRepository.findAll().shuffled()[0]
  }

  @Transactional
  fun responseAddMenu(name: String): Menu {
    if (menuRepository.existsByName(name)) throw MenuAlreadyExistsException()
    return menuRepository.save(Menu(name))
  }

  @Transactional
  fun responseAllMenu(): List<Menu> {
    return menuRepository.findAll()
  }

  @Transactional
  fun responseRemoveMenu(name: String) {
    menuRepository.findByName(name)
      ?.apply { menuRepository.delete(this) }
      ?: throw NoSuchMenuException()
  }
}