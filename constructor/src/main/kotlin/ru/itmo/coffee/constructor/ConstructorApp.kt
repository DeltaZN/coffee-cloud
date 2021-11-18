package ru.itmo.coffee.constructor

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.PropertySource
import ru.itmo.coffee.constructor.service.IngredientsService

fun main(args: Array<String>) {
    runApplication<ConstructorApp>(*args)
}

@SpringBootApplication
@PropertySource("classpath:kafka.properties")
open class ConstructorApp(
    private val ingredientsService: IngredientsService,
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        if (ingredientsService.getAllIngredients().isEmpty())
            ingredientsService.fetchIngredientsFromStore()
    }
}