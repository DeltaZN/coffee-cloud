package ru.itmo.coffee.constructor.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import ru.itmo.coffee.MessageResponse
import ru.itmo.coffee.MessageWithIdResponse
import ru.itmo.coffee.constructor.dto.CoffeeComponentDTO
import ru.itmo.coffee.constructor.dto.CoffeeRecipeDTO
import ru.itmo.coffee.constructor.entity.CoffeeRecipe
import ru.itmo.coffee.constructor.entity.RecipeComponent
import ru.itmo.coffee.constructor.repository.CoffeeRecipeJpaRepository
import ru.itmo.coffee.constructor.repository.IngredientJpaRepository
import ru.itmo.coffee.constructor.repository.RecipeComponentJpaRepository
import ru.itmo.coffee.kafka.*
import ru.itmo.coffee.notFound
import java.time.Instant
import javax.persistence.EntityNotFoundException

@Service
class CoffeeRecipeService(
    private val coffeeRecipeJpaRepository: CoffeeRecipeJpaRepository,
    private val ingredientJpaRepository: IngredientJpaRepository,
    private val recipeComponentJpaRepository: RecipeComponentJpaRepository,
    private val kafkaRecipesTemplate: KafkaTemplate<Long, RecipeMessageKafkaDTO>,
) {

    @Value("\${kafka.enabled}")
    private val kafkaEnabled: Boolean = false

    fun getAllRecipes(): List<CoffeeRecipe> = coffeeRecipeJpaRepository.findAll().toList()

    fun getRecipe(id: Long): CoffeeRecipe = coffeeRecipeJpaRepository.findById(id)
        .orElseThrow { EntityNotFoundException(notFound("Recipe", id)) }

    fun createRecipe(recipe: CoffeeRecipeDTO): MessageWithIdResponse {
        val entity = CoffeeRecipe(0, recipe.name ?: "", components = mutableListOf())
        coffeeRecipeJpaRepository.save(entity)
        val recipeComponents = recipe.components?.map { dto -> mapRecipeComponentDTO(dto, entity) }
        recipeComponents?.forEach(recipeComponentJpaRepository::save)
        coffeeRecipeJpaRepository.save(entity)
        sendRecipeUpdate(RecipeMessageKafkaDTO(entity.id, MessageType.CREATE,
            CoffeeRecipeKafkaDTO(
                entity.name,
                entity.creationTime,
                entity.modificationTime,
                entity.components.map { r ->
                    RecipeComponentKafkaDTO(
                        r.ingredient.id,
                        r.insertionOrder,
                        r.quantity,
                    )
                }
            )
        ))
        return MessageWithIdResponse("Recipe was created.", entity.id)
    }

    fun editRecipe(id: Long, recipe: CoffeeRecipeDTO): MessageResponse {
        val entity = coffeeRecipeJpaRepository.findById(id).orElseThrow {
            EntityNotFoundException(notFound("Recipe", id))
        }
        recipe.name?.let { entity.name = it }
        recipe.components?.let { entity.components = it.map { dto -> mapRecipeComponentDTO(dto, entity) } }
        entity.modificationTime = Instant.now()
        entity.components.forEach(recipeComponentJpaRepository::save)
        coffeeRecipeJpaRepository.save(entity)
        sendRecipeUpdate(RecipeMessageKafkaDTO(entity.id, MessageType.EDIT,
            CoffeeRecipeKafkaDTO(
                entity.name,
                entity.creationTime,
                entity.modificationTime,
                entity.components.map { r ->
                    RecipeComponentKafkaDTO(
                        r.ingredient.id,
                        r.insertionOrder,
                        r.quantity,
                    )
                }
            )
        ))
        return MessageResponse("Recipe was edited.")
    }

    fun deleteRecipe(id: Long): MessageResponse {
        val entity = coffeeRecipeJpaRepository.findById(id).orElseThrow {
            EntityNotFoundException(notFound("Recipe", id))
        }
        coffeeRecipeJpaRepository.delete(entity)
        sendRecipeUpdate(RecipeMessageKafkaDTO(entity.id, MessageType.DELETE))
        return MessageResponse("Recipe was deleted.")
    }

    private fun sendRecipeUpdate(dto: RecipeMessageKafkaDTO) {
        if (kafkaEnabled) kafkaRecipesTemplate.send(KAFKA_RECIPES_TOPIC, dto)
    }

    private fun mapRecipeComponentDTO(dto: CoffeeComponentDTO, entity: CoffeeRecipe): RecipeComponent =
        RecipeComponent(
            0,
            entity,
            ingredientJpaRepository.findById(dto.ingredientId).orElseThrow {
                EntityNotFoundException(notFound("Ingredient", dto.ingredientId))
            },
            dto.quantity,
            dto.insertionOrder,
        )
}