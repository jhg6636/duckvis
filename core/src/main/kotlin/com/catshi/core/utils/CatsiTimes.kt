package com.catshi.core.utils

inline class Hour(val value: Int)
inline class Minute(val value: Int)
inline class Second(private val value: Int) {

    fun toMinute(): Minute {
        return Minute(value / 60)
    }

    operator fun minus(other: Second): Second {
        return Second(this.value - other.value)
    }

}
