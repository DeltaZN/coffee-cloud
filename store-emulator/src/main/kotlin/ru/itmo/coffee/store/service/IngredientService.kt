package ru.itmo.coffee.store.service

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.itmo.coffee.*
import ru.itmo.coffee.store.dto.IngredientDTO
import ru.itmo.coffee.store.entity.Ingredient
import ru.itmo.coffee.store.repository.IngredientJpaRepository
import javax.persistence.EntityNotFoundException

@Service
class IngredientService(
    private val ingredientJpaRepository: IngredientJpaRepository,
    private val kafkaIngredientsTemplate: KafkaTemplate<Long, IngredientMessageKafkaDTO>,
) {
    fun getAllIngredients(): List<Ingredient> = ingredientJpaRepository.findAll().toList()

    fun addIngredient(ingredient: IngredientDTO): MessageResponse {
        val entity = Ingredient(0, ingredient.name ?: "", ingredient.cost ?: 0.0, ingredient.calories ?: 0.0, ingredient.volumeMl ?: 0.0)
        ingredientJpaRepository.save(entity)
        sendIngredientUpdate(IngredientMessageKafkaDTO(entity.id, MessageType.CREATE, IngredientKafkaDTO(
            entity.name,
            entity.cost,
            entity.calories,
            entity.volumeMl,
        )))
        return MessageResponse("Ingredient was added.")
    }

    fun editIngredient(id: Long, ingredient: IngredientDTO): MessageResponse {
        val entity = ingredientJpaRepository.findById(id).orElseThrow {
            EntityNotFoundException("Ingredient with id $id wasn't found!")
        }
        ingredient.name?.let { entity.name = it }
        ingredient.cost?.let { entity.cost = it }
        ingredient.calories?.let { entity.calories = it }
        ingredient.volumeMl?.let { entity.volumeMl = it }
        ingredientJpaRepository.save(entity)
        sendIngredientUpdate(IngredientMessageKafkaDTO(entity.id, MessageType.EDIT, IngredientKafkaDTO(
            entity.name,
            entity.cost,
            entity.calories,
            entity.volumeMl,
        )))
        return MessageResponse("Ingredient was edited.")
    }

    fun deleteIngredient(id: Long): MessageResponse {
        val entity = ingredientJpaRepository.findById(id).orElseThrow {
            EntityNotFoundException("Ingredient with id $id wasn't found!")
        }
        ingredientJpaRepository.delete(entity)
        sendIngredientUpdate(IngredientMessageKafkaDTO(entity.id, MessageType.DELETE))
        return MessageResponse("Ingredient was deleted.")
    }

    private fun sendIngredientUpdate(dto: IngredientMessageKafkaDTO) {
        kafkaIngredientsTemplate.send(KAFKA_INGREDIENTS_TOPIC, dto)
    }

}