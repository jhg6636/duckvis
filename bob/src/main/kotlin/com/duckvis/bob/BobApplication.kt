package com.duckvis.bob

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import javax.annotation.PostConstruct

@SpringBootApplication
@EnableJpaRepositories(basePackages = ["com.duckvis.bob", "com.duckvis.core"])
@EntityScan(basePackages = ["com.duckvis"])
@EnableScheduling
class BobApplication {
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