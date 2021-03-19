package com.catshi.nuguri

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.PropertySource
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import javax.annotation.PostConstruct

@SpringBootApplication
@EnableJpaRepositories(basePackages = ["com.catshi"])
@EntityScan(basePackages = ["com.catshi"])
open class NuguriApplication {
    @PostConstruct
    private fun started() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC.id))
        println("현재시각: ${LocalDateTime.now()}")
    }
}

fun main(args: Array<String>) {
    runApplication<NuguriApplication>(*args)
}
