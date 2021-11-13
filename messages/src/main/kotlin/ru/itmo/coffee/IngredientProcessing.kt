package ru.itmo.coffee

const val KAFKA_INGREDIENTS_TOPIC = "server.ingredients"

data class IngredientMessageKafkaDTO(
    val ingredientId: Long = 0,
    val action: MessageType = MessageType.EDIT,
    val ingredient: IngredientKafkaDTO? = null,
)

data class IngredientKafkaDTO(
    val name: String? = null,
    val cost: Double? = null,
    val calories: Double? = null,
    val volumesMl: Double? = null,
)