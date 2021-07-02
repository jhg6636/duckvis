package com.duckvis.nuguri.dtos

import com.duckvis.core.dtos.nuguri.WorkTypeDto
import org.junit.jupiter.api.Test

internal class WorkDtoTypeRequestTest {
  @Test
  fun x() {
    val x = WorkTypeDto(false, false, false)
    val y = WorkTypeDto(false, false, false)
    val Z = WorkTypeDto(false, true, false)

    val a = x.copy(isNight = true)
    println(x == y)
    println(y == Z)
//        println(WorkTypeDto(false, true, false))
  }
}