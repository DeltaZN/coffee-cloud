package ru.itmo.coffee.constructor.controller

import ru.itmo.coffee.constructor.repository.RecipeComponentJpaRepository
import org.springframework.web.bind.annotation.*
import ru.itmo.coffee.MessageResponse
import ru.itmo.coffee.constructor.dto.CoffeeRecipeDTO
import ru.itmo.coffee.constructor.entity.CoffeeRecipe
import ru.itmo.coffee.constructor.entity.Ingredient
import ru.itmo.coffee.constructor.repository.CoffeeRecipeJpaRepository
import ru.itmo.coffee.constructor.repository.IngredientJpaRepository
import ru.itmo.coffee.constructor.service.CoffeeRecipeService
import java.util.*

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/constructor")
class CoffeeConstructorController (
    private val ingredientJpaRepository: IngredientJpaRepository,
    private val coffeeRecipeService: CoffeeRecipeService,
) {

    @GetMapping("ingredients")
    fun getIngredients(): List<Ingredient> =
        ingredientJpaRepository.findAll().toList()

    @GetMapping("ingredients/{id}")
    fun getIngredient(@PathVariable("id") id: Long): Optional<Ingredient> = ingredientJpaRepository.findById(id);

    @GetMapping("recipes")
    fun getCoffeeRecipes(): List<CoffeeRecipe> =
        coffeeRecipeService.getAllRecipes()

    @PostMapping("recipes")
    fun createCoffeeRecipe(@RequestBody payload: CoffeeRecipeDTO): MessageResponse =
        coffeeRecipeService.createRecipe(payload)

    @PutMapping("recipes/{id}")
    fun editCustomCoffeeRecipe(@PathVariable("id") id: Long, @RequestBody payload: CoffeeRecipeDTO): MessageResponse =
        coffeeRecipeService.editRecipe(id, payload)

    @DeleteMapping("recipes/{id}")
    fun deleteCustomCoffeeRecipe(@PathVariable("id") id: Long) =
        coffeeRecipeService.deleteRecipe(id)

}