package com.catshi.bob.types

import com.catshi.core.types.CountryType

enum class BobStyleType {
    ANYTHING,
    VEGETARIAN;

    override fun toString(): String {
        return when (this) {
            ANYTHING -> "밥"
            VEGETARIAN -> "채식"
        }
    }
//    fun toString(countryType: CountryType): String {
//        return when (this) {
//            ANYTHING -> when (countryType) {
//                CountryType.KOREA -> "밥"
//                CountryType.US -> "Meal"
//            }
//            VEGETARIAN -> when (countryType) {
//                CountryType.KOREA -> "채식"
//                CountryType.US -> "Vegetarian Menu"
//            }
//        }
//    }
}