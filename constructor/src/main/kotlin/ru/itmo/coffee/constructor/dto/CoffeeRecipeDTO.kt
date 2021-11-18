package ru.itmo.coffee.constructor.dto


data class CoffeeRecipeDTO(
    val coffeeId: Long? = null,
    val name: String? = null,
    val components: List<CoffeeComponentDTO>? = null,
)

data class CoffeeComponentDTO(
    val ingredientId: Long = 0,
    val insertionOrder: Int = 0,
    val quantity: Double = 0.0,
)


