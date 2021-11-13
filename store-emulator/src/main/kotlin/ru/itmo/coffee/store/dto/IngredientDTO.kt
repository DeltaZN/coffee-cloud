package ru.itmo.coffee.store.dto

data class IngredientDTO(
    val id: Long? = null,
    val name: String? = null,
    val cost: Double? = null,
    val calories: Double? = null,
    val volumeMl: Double? = null,
)