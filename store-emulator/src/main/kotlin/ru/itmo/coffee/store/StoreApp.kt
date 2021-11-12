package ru.itmo.coffee.store

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

fun main(args: Array<String>) {
    runApplication<StoreApp>(*args)
}

@SpringBootApplication
open class StoreApp