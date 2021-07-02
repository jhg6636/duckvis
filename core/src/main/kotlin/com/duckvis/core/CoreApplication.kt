package com.duckvis.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class CoreApplication

fun main(args: Array<String>) {
  runApplication<CoreApplication>(*args)
}
