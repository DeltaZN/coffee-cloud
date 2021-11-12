package ru.itmo.coffee.constructor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

fun main(args: Array<String>) {
    runApplication<ConstructorApp>(*args)
}

@SpringBootApplication open class ConstructorApp