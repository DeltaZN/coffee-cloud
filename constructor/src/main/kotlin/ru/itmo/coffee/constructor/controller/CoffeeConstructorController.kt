package ru.itmo.coffee.constructor.controller

import ru.itmo.coffee.constructor.repository.RecipeComponentJpaRepository
import org.springframework.web.bind.annotation.*
import ru.itmo.coffee.constructor.dto.ConstructorMessageDTO
import ru.itmo.coffee.constructor.dto.MessageResponse
import ru.itmo.coffee.constructor.entity.CoffeeRecipe
import ru.itmo.coffee.constructor.entity.Ingredient
import ru.itmo.coffee.constructor.repository.CoffeeRecipeJpaRepository
import ru.itmo.coffee.constructor.repository.IngredientJpaRepository
import java.util.*

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/constructor")
class CoffeeConstructorController (
    private val ingredientJpaRepository: IngredientJpaRepository,
    private val coffeeRecipeJpaRepository: CoffeeRecipeJpaRepository,
    private val recipeComponentJpaRepository: RecipeComponentJpaRepository,
) {

    @GetMapping("ingredients")
    fun getIngredients(): List<Ingredient> = ingredientJpaRepository.findAll().toList()

    @GetMapping("ingredients/{id}")
    fun getIngredient(@PathVariable("id") id: Long): Optional<Ingredient> = ingredientJpaRepository.findById(id);

    @GetMapping("recipes")
    fun getCoffeeRecipes(): List<CoffeeRecipe> = coffeeRecipeJpaRepository.findAll().toList()

    @PostMapping("recipes")
    fun addCoffeeRecipe(@RequestBody payload: ConstructorMessageDTO): MessageResponse {
//        coffeeRecipeJpaRepository.save()
        return MessageResponse("Successfully added a coffee recipe!")
    }

    @PutMapping("recipes")
    fun editCustomCoffee(@RequestBody payload: ConstructorMessageDTO): MessageResponse {
//        coffeeService.editCustomCoffee(payload)
        return MessageResponse("Successfully edited a coffee recipe!")
    }
}