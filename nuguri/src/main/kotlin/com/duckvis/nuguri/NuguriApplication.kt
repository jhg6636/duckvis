package com.duckvis.nuguri

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import javax.annotation.PostConstruct

@SpringBootApplication
@ComponentScan(basePackages = ["com.duckvis.core", "com.duckvis.nuguri"])
class NuguriApplication {
  @PostConstruct
  private fun started() {
    TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC.id))
    println("현재시각: ${LocalDateTime.now()}")
  }
}

fun main(args: Array<String>) {
  runApplication<NuguriApplication>(*args)
}
