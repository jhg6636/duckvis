package com.duckvis.slack

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import javax.annotation.PostConstruct


@EnableJpaRepositories(basePackages = ["com.duckvis.core", "com.duckvis.nuguri", "com.duckvis.bob"])
@EntityScan(basePackages = ["com.duckvis"])
@ComponentScan(basePackages = ["com.duckvis"])
@SpringBootApplication
class SlackApplication {
  @PostConstruct
  private fun started() {
    TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC.id))
    println("현재시각: ${LocalDateTime.now()}")
    // TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
  }
}

fun main(args: Array<String>) {
  runApplication<SlackApplication>(*args)
}
