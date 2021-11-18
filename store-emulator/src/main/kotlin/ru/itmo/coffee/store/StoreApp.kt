package ru.itmo.coffee.store

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.PropertySource
import ru.itmo.coffee.store.entity.Coffee
import ru.itmo.coffee.store.entity.CoffeeType
import ru.itmo.coffee.store.entity.Ingredient
import ru.itmo.coffee.store.repository.CoffeeJpaRepository
import ru.itmo.coffee.store.repository.IngredientJpaRepository

fun main(args: Array<String>) {
    runApplication<StoreApp>(*args)
}

@SpringBootApplication
@PropertySource("classpath:kafka.properties")
open class StoreApp(
    private val coffeeJpaRepository: CoffeeJpaRepository,
    private val ingredientJpaRepository: IngredientJpaRepository,
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        addDefaultIngredients()
        addDefaultCoffees()
    }

    private fun addDefaultCoffees() = listOf(
        Coffee(0, "Cappuccino", 99.99, CoffeeType.STANDARD, null),
        Coffee(0, "Latte", 109.99, CoffeeType.STANDARD, null),
        Coffee(0, "Americano", 79.99, CoffeeType.STANDARD, null),
        Coffee(0, "Raf", 109.99, CoffeeType.STANDARD, null),
    ).forEach(coffeeJpaRepository::save)

    private fun addDefaultIngredients() = listOf(
        Ingredient(0, "Espresso", 25.0, 100.0),
        Ingredient(0, "Chocolate chips", 25.0, 100.0),
        Ingredient(0, "Waffle sprinkling", 25.0, 100.0),
        Ingredient(0, "Raspberry syrup", 25.0, 100.0),
        Ingredient(0, "Almond syrup", 25.0, 100.0),
        Ingredient(0, "Chocolate syrup", 25.0, 100.0),
        Ingredient(0, "Strawberry syrup", 25.0, 100.0),
        Ingredient(0, "Condensed milk", 25.0, 100.0),
        Ingredient(0, "Liquor", 25.0, 100.0),
        Ingredient(0, "Espresso", 25.0, 100.0),
        Ingredient(0, "Chocolate chips", 25.0, 100.0),
        Ingredient(0, "Maple syrup", 25.0, 100.0),
        Ingredient(0, "Marshmallow", 25.0, 100.0),
        Ingredient(0, "Carnation", 25.0, 100.0),
        Ingredient(0, "Grated ginger", 25.0, 100.0),
        Ingredient(0, "Nutmeg", 25.0, 100.0),
        Ingredient(0, "Cinnamon", 25.0, 100.0),
        Ingredient(0, "Coconut milk", 25.0, 100.0),
        Ingredient(0, "Whipped cream", 25.0, 100.0),
        Ingredient(0, "Milk", 25.0, 100.0),
    ).forEach(ingredientJpaRepository::save)

}