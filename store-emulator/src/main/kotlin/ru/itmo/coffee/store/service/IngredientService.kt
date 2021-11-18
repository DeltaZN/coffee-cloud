package ru.itmo.coffee.store.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.itmo.coffee.*
import ru.itmo.coffee.dto.IngredientDTO
import ru.itmo.coffee.kafka.IngredientKafkaDTO
import ru.itmo.coffee.kafka.IngredientMessageKafkaDTO
import ru.itmo.coffee.kafka.KAFKA_INGREDIENTS_TOPIC
import ru.itmo.coffee.kafka.MessageType
import ru.itmo.coffee.store.entity.CoffeeRecipe
import ru.itmo.coffee.store.entity.Ingredient
import ru.itmo.coffee.store.repository.CoffeeJpaRepository
import ru.itmo.coffee.store.repository.CoffeeRecipeJpaRepository
import ru.itmo.coffee.store.repository.IngredientJpaRepository
import ru.itmo.coffee.store.repository.RecipeComponentJpaRepository
import javax.persistence.EntityNotFoundException

@Service
class IngredientService(
    private val ingredientJpaRepository: IngredientJpaRepository,
    private val kafkaIngredientsTemplate: KafkaTemplate<Long, IngredientMessageKafkaDTO>,
    private val coffeeJpaRepository: CoffeeJpaRepository,
    private val coffeeRecipeJpaRepository: CoffeeRecipeJpaRepository,
    private val recipeComponentJpaRepository: RecipeComponentJpaRepository,
) {

    @Value("\${kafka.enabled}")
    private val kafkaEnabled: Boolean = false

    fun getAllIngredients(): List<IngredientDTO> = ingredientJpaRepository.findAll().map { i ->
        IngredientDTO(i.id, i.name, i.cost, i.calories, i.volumeMl)
    }

    fun getIngredient(id: Long): IngredientDTO {
        val i = ingredientJpaRepository.findById(id).orElseThrow {
            EntityNotFoundException(notFound("Ingredient", id))
        }
        return IngredientDTO(i.id, i.name, i.cost, i.calories, i.volumeMl)
    }

    fun addIngredient(ingredient: IngredientDTO): MessageWithIdResponse {
        val entity = Ingredient(
            0,
            ingredient.name ?: "",
            ingredient.cost ?: 0.0,
            ingredient.calories ?: 0.0,
            ingredient.volumeMl ?: 0.0
        )
        ingredientJpaRepository.save(entity)
        sendIngredientUpdate(
            IngredientMessageKafkaDTO(
                entity.id, MessageType.CREATE, IngredientKafkaDTO(
                    entity.name,
                    entity.cost,
                    entity.calories,
                    entity.volumeMl,
                )
            )
        )
        return MessageWithIdResponse("Ingredient was added.", entity.id)
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
        sendIngredientUpdate(
            IngredientMessageKafkaDTO(
                entity.id, MessageType.EDIT, IngredientKafkaDTO(
                    entity.name,
                    entity.cost,
                    entity.calories,
                    entity.volumeMl,
                )
            )
        )
        return MessageResponse("Ingredient was edited.")
    }

    fun deleteIngredient(id: Long): MessageResponse {
        val entity = ingredientJpaRepository.findById(id).orElseThrow {
            EntityNotFoundException("Ingredient with id $id wasn't found!")
        }
        val recipes = mutableSetOf<CoffeeRecipe>()
        entity.recipeComponents.forEach { c ->
            val recipe = c.recipe
            recipe?.let { recipes.add(it) }
        }
        recipes.forEach {
            val coffee = it.coffee
            val components = it.components
            it.components = emptyList()
            components.forEach { c -> c.recipe = null; c.ingredient = null; recipeComponentJpaRepository.delete(c); }
            it.coffee = null
            coffeeRecipeJpaRepository.delete(it)
            coffee?.let { c ->
                c.recipe = null
                coffeeJpaRepository.delete(c)
            }
            coffeeRecipeJpaRepository.delete(it)
        }
        ingredientJpaRepository.delete(entity)
        sendIngredientUpdate(IngredientMessageKafkaDTO(entity.id, MessageType.DELETE))
        return MessageResponse("Ingredient was deleted.")
    }

    private fun sendIngredientUpdate(dto: IngredientMessageKafkaDTO) {
        if (kafkaEnabled) kafkaIngredientsTemplate.send(KAFKA_INGREDIENTS_TOPIC, dto)
    }

}