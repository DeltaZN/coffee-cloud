package ru.itmo.coffee.constructor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.PropertySource

fun main(args: Array<String>) {
    runApplication<ConstructorApp>(*args)
}

@SpringBootApplication
@PropertySource("classpath:kafka.properties")
open class ConstructorApp