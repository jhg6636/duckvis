package com.duckvis.core.types.shared

import com.duckvis.core.exceptions.shared.NoSuchCityException

enum class CityType(val cityName: String, val cityNickname: String) {
  SEOUL("서울", "ㅅㅇ"),
  DAEJEON("대전", "ㄷㅈ"),
  LA("LA", "la"),
  NY("NY", "ny");

  companion object {
    fun of(text: String): CityType {
      return values().firstOrNull { it.cityName == text || it.cityNickname == text } ?: throw NoSuchCityException()
    }
  }

  override fun toString(): String {
    return this.cityName
  }
}