package ru.itmo.coffee.constructor.dto


data class CoffeeRecipeDTO(
    val coffeeId: Long? = null,
    val name: String? = null,
    val components: List<CoffeeComponentDTO>? = null,
)

data class CoffeeComponentDTO(
    val id: Long,
    val ingredientId: Long,
    val insertionOrder: Int,
    val quantity: Double,
)


