package ru.itmo.coffee

import java.time.ZonedDateTime

const val KAFKA_RECIPES_TOPIC = "server.recipes"

data class RecipeMessageKafkaDTO(
    val recipeId: Long = 0,
    val action: MessageType = MessageType.EDIT,
    val coffeeRecipe: CoffeeRecipeKafkaDTO? = null,
)

data class CoffeeRecipeKafkaDTO(
    val name: String? = null,
    val creationTime: ZonedDateTime? = null,
    val modificationTime: ZonedDateTime? = null,
    val components: List<RecipeComponentKafkaDTO>? = null,
)

data class RecipeComponentKafkaDTO (
    val ingredientId: Long = 0,
    val insertionOrder: Int = 0,
    val quantity: Double = 0.0,
)