package ru.itmo.coffee.store.controller

import org.springframework.web.bind.annotation.*
import ru.itmo.coffee.store.dto.IngredientDTO
import ru.itmo.coffee.store.entity.Ingredient
import ru.itmo.coffee.store.repository.IngredientJpaRepository
import ru.itmo.coffee.store.service.IngredientService

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/store")
class CoffeeStoreController(
    private val ingredientService: IngredientService,
) {
    @GetMapping("ingredients")
    fun getIngredients(): List<Ingredient> = ingredientService.getAllIngredients()

    @PostMapping("ingredients")
    fun addIngredient(@RequestBody dto: IngredientDTO) = ingredientService.addIngredient(dto)

    @PutMapping("ingredients/{id}")
    fun updateIngredient(@PathVariable("id") id: Long, @RequestBody dto: IngredientDTO) =
        ingredientService.editIngredient(id, dto)

    @DeleteMapping("ingredients/{id}")
    fun deleteIngredient(@PathVariable("id") id: Long) = ingredientService.deleteIngredient(id)

    @GetMapping("coffees")
    fun getCoffee() = {}
}