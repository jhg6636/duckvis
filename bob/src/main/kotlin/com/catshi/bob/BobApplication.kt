package com.catshi.bob

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.PropertySource
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZoneOffset.UTC
import java.util.*
import javax.annotation.PostConstruct

@SpringBootApplication
@EnableJpaRepositories(basePackages = ["com.catshi.bob", "com.catshi.core"])
@EntityScan(basePackages = ["com.catshi"])
@EnableScheduling
open class BobApplication {
    @PostConstruct
    private fun started() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC.id))
        println("현재시각: ${LocalDateTime.now()}")
        // TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
    }
}

fun main(args: Array<String>) {
    runApplication<BobApplication>(*args)
}