package io.fbex.flixd.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FlixdBackendApplication

fun main(args: Array<String>) {
    runApplication<FlixdBackendApplication>(*args)
}
