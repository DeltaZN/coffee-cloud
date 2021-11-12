package ru.itmo.coffee.constructor.dto

import ru.itmo.coffee.constructor.entity.CoffeeRecipe

enum class Action {
    delete,
    create,
    edit
}

data class ConstructorMessageDTO(
    val coffeeId: Long = 0,
    val action: Action = Action.edit,
    val coffee: CoffeeRecipeDTO? = null,
)

data class CoffeeRecipeDTO(
    val name: String?,
    val components: List<CoffeeComponentDTO>? = mutableListOf(),
)

data class CoffeeComponentDTO(
    val id: Long,
    val ingredientId: Long,
    val insertionOrder: Int,
    val quantity: Int,
)
