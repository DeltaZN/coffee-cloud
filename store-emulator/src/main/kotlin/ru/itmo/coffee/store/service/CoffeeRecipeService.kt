package ru.itmo.coffee.store.service

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import ru.itmo.coffee.kafka.KAFKA_RECIPES_TOPIC
import ru.itmo.coffee.kafka.MessageType
import ru.itmo.coffee.kafka.RecipeMessageKafkaDTO
import ru.itmo.coffee.store.entity.Coffee
import ru.itmo.coffee.store.entity.CoffeeRecipe
import ru.itmo.coffee.store.entity.CoffeeType
import ru.itmo.coffee.store.entity.RecipeComponent
import ru.itmo.coffee.store.repository.CoffeeJpaRepository
import ru.itmo.coffee.store.repository.CoffeeRecipeJpaRepository
import ru.itmo.coffee.store.repository.IngredientJpaRepository
import ru.itmo.coffee.store.repository.RecipeComponentJpaRepository
import java.time.Instant
import java.time.ZonedDateTime
import javax.persistence.EntityNotFoundException

@Service
class CoffeeRecipeService(
    private val coffeeJpaRepository: CoffeeJpaRepository,
    private val coffeeRecipeJpaRepository: CoffeeRecipeJpaRepository,
    private val recipeComponentJpaRepository: RecipeComponentJpaRepository,
    private val ingredientJpaRepository: IngredientJpaRepository,
) {
    @KafkaListener(id = "store_recipes", topics = [KAFKA_RECIPES_TOPIC], containerFactory = "singleFactory")
    fun consumeRecipe(dto: RecipeMessageKafkaDTO) {
        when (dto.action) {
            MessageType.CREATE -> createRecipe(dto)
            MessageType.EDIT -> editRecipe(dto)
            MessageType.DELETE -> deleteRecipe(dto)
        }
    }

    private fun createRecipe(dto: RecipeMessageKafkaDTO) {
        if (!coffeeRecipeJpaRepository.findById(dto.recipeId).isPresent) {
            val entity = CoffeeRecipe(
                dto.recipeId,
                dto.coffeeRecipe?.name ?: "",
                dto.coffeeRecipe?.creationTime ?: Instant.now(),
                dto.coffeeRecipe?.modificationTime ?: Instant.now(),
                mutableListOf(),
            )
            coffeeRecipeJpaRepository.save(entity)
            val components = dto.coffeeRecipe?.components?.map { c -> RecipeComponent(
                0,
                entity,
                ingredientJpaRepository.findById(c.ingredientId).orElseThrow {
                    EntityNotFoundException("Ingredient with id ${c.ingredientId} wasn't found")
                },
                c.quantity,
                c.insertionOrder,
            ) }
            components?.forEach(recipeComponentJpaRepository::save)
            coffeeRecipeJpaRepository.save(entity)
            // TODO count the cost
            val coffee = Coffee(0, entity.name, 0.0, CoffeeType.USER, entity)
            coffeeJpaRepository.save(coffee)
        }
    }

    private fun editRecipe(dto: RecipeMessageKafkaDTO) {
        val optional = coffeeRecipeJpaRepository.findById(dto.recipeId)

        if (optional.isPresent) {
            val entity = optional.get()

            val components = dto.coffeeRecipe?.components?.map { c ->
                RecipeComponent(
                    0,
                    entity,
                    ingredientJpaRepository.findById(c.ingredientId).orElseThrow {
                        EntityNotFoundException("Ingredient with id ${c.ingredientId} wasn't found")
                    },
                    c.quantity,
                    c.insertionOrder,
                )
            }
            components?.forEach(recipeComponentJpaRepository::save)

            dto.coffeeRecipe?.name?.let { entity.name = it }
            dto.coffeeRecipe?.modificationTime?.let { entity.modificationTime = it }
            dto.coffeeRecipe?.components?.let { entity.components = components!! }
            coffeeRecipeJpaRepository.save(entity)

            // TODO update the cost
            dto.coffeeRecipe?.components?.let {
                entity.coffee!!.cost = 0.0
                coffeeJpaRepository.save(entity.coffee!!)
            }
        } else {
            createRecipe(dto)
        }
    }

    private fun deleteRecipe(dto: RecipeMessageKafkaDTO) {
        val recipe = coffeeRecipeJpaRepository.findById(dto.recipeId).orElseThrow {
            EntityNotFoundException("Coffee recipe with id ${dto.recipeId} wasn't found")
        }
        coffeeJpaRepository.delete(recipe.coffee!!)
    }
}