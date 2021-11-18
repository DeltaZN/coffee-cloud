package ru.itmo.coffee.constructor.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import ru.itmo.coffee.constructor.entity.Ingredient
import ru.itmo.coffee.constructor.repository.IngredientJpaRepository
import ru.itmo.coffee.dto.IngredientDTO
import ru.itmo.coffee.kafka.IngredientMessageKafkaDTO
import ru.itmo.coffee.kafka.KAFKA_INGREDIENTS_TOPIC
import ru.itmo.coffee.kafka.MessageType
import javax.persistence.EntityNotFoundException


@Service
class IngredientsService(
    private val ingredientJpaRepository: IngredientJpaRepository,
) {

    @Value("\${store.url}")
    private val storeUrl: String = ""

    fun getAllIngredients(): List<Ingredient> =
        ingredientJpaRepository.findAll().toList()

    fun fetchIngredientsFromStore() {
        val restTemplate = RestTemplate()
        val response = restTemplate.getForObject(storeUrl, Array<IngredientDTO>::class.java)
        response?.map { i -> Ingredient(i.id ?: 0, i.name ?: "", i.cost ?: 0.0, i.calories ?: 0.0, i.volumeMl ?: 0.0) }
            ?.forEach { ingredientJpaRepository.save(it) }
    }

    @KafkaListener(id = "constructor_ingredients", topics = [KAFKA_INGREDIENTS_TOPIC], containerFactory = "singleFactory")
    fun consumeIngredient(dto: IngredientMessageKafkaDTO) {
        when (dto.action) {
            MessageType.CREATE -> createIngredient(dto)
            MessageType.EDIT -> editIngredient(dto)
            MessageType.DELETE -> deleteIngredient(dto)
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

    private fun deleteIngredient(dto: IngredientMessageKafkaDTO) {
        val ingredient = ingredientJpaRepository.findById(dto.ingredientId).orElseThrow {
            EntityNotFoundException("Ingredient with id ${dto.ingredientId} wasn't found")
        }
        ingredientJpaRepository.delete(ingredient)
    }
}