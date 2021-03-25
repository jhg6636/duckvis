package com.duckvis.core.types

import com.duckvis.core.exceptions.NoSuchCityException

enum class CityType(val cityName: String) {
    SEOUL("서울"),
    DAEJEON("대전"),
    LA("LA"),
    NY("NY");

    companion object {
        fun of(text: String): CityType {
            return values().firstOrNull { it.cityName == text } ?: throw NoSuchCityException()
        }
    }

    override fun toString(): String {
        return this.cityName
    }
}