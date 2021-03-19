package com.catshi.core.types

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CityTypeTest {
    @Test
    fun `스트링에서 도시타입 리턴`() {
        val city = CityType.of("서울")

        println(city)
    }
}