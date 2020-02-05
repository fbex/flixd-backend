package io.fbex.flixd.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class FlixdBackendApplication

fun main(args: Array<String>) {
    runApplication<FlixdBackendApplication>(*args)
}
