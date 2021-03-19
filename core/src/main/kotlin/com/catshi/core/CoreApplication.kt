package com.catshi.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.PropertySource

@SpringBootApplication
open class CoreApplication

fun main(args: Array<String>) {
    runApplication<CoreApplication>(*args)
}
