package com.duckvis.nuguri.ui

import com.duckvis.nuguri.services.NuguriService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class NuguriSlackControllerTest(
  @Autowired private val nuguriService: List<NuguriService>
) {
  @Test
  fun x() {
    println(nuguriService)
    println(nuguriService.size)
  }
}