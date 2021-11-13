package ru.itmo.coffee.constructor.service

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import ru.itmo.coffee.IngredientMessageKafkaDTO
import ru.itmo.coffee.KAFKA_INGREDIENTS_TOPIC
import ru.itmo.coffee.MessageType
import ru.itmo.coffee.constructor.entity.Ingredient
import ru.itmo.coffee.constructor.repository.IngredientJpaRepository
import javax.persistence.EntityNotFoundException

@Service
class IngredientsService(
    private val ingredientJpaRepository: IngredientJpaRepository,
) {
    @KafkaListener(id = "constructor_ingredients", topics = [KAFKA_INGREDIENTS_TOPIC], containerFactory = "singleFactory")
    fun consumeIngredient(dto: IngredientMessageKafkaDTO) {
        when (dto.action) {
            MessageType.CREATE -> createIngredient(dto)
            MessageType.EDIT -> editIngredient(dto)
            MessageType.DELETE -> ingredientJpaRepository.deleteById(dto.ingredientId)
        }
    }

    private fun createIngredient(dto: IngredientMessageKafkaDTO) {
        if (!ingredientJpaRepository.findById(dto.ingredientId).isPresent) {
            val entity = Ingredient(
                dto.ingredientId,
                dto.ingredient?.name ?: "",
                dto.ingredient?.cost ?: 0.0,
                dto.ingredient?.calories ?: 0.0,
                dto.ingredient?.volumesMl ?: 0.0,
            )
            ingredientJpaRepository.save(entity)
        }
    }

    private fun editIngredient(dto: IngredientMessageKafkaDTO) {
        val entity = ingredientJpaRepository.findById(dto.ingredientId).orElseThrow {
            EntityNotFoundException()
        }

        dto.ingredient?.name?.let { entity.name = it }
        dto.ingredient?.cost?.let { entity.cost = it }
        dto.ingredient?.calories?.let { entity.calories = it }
        dto.ingredient?.volumesMl?.let { entity.volumeMl = it }

        ingredientJpaRepository.save(entity)
    }
}