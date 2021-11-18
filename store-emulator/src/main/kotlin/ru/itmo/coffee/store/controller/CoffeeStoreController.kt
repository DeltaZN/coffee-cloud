package ru.itmo.coffee.store.controller

import org.springframework.web.bind.annotation.*
import ru.itmo.coffee.MessageWithIdResponse
import ru.itmo.coffee.dto.IngredientDTO
import ru.itmo.coffee.notFound
import ru.itmo.coffee.store.entity.Coffee
import ru.itmo.coffee.store.repository.CoffeeJpaRepository
import ru.itmo.coffee.store.service.IngredientService
import javax.persistence.EntityNotFoundException

@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/store")
class CoffeeStoreController(
    private val ingredientService: IngredientService,
    private val coffeeJpaRepository: CoffeeJpaRepository,
) {
    @GetMapping("ingredients")
    fun getIngredients(): List<IngredientDTO> =
        ingredientService.getAllIngredients()

    @GetMapping("ingredients/{id}")
    fun getIngredient(@PathVariable id: Long): IngredientDTO =
        ingredientService.getIngredient(id)

    @PostMapping("ingredients")
    fun addIngredient(@RequestBody dto: IngredientDTO): MessageWithIdResponse =
        ingredientService.addIngredient(dto)

    @PutMapping("ingredients/{id}")
    fun updateIngredient(@PathVariable("id") id: Long, @RequestBody dto: IngredientDTO) =
        ingredientService.editIngredient(id, dto)

    @DeleteMapping("ingredients/{id}")
    fun deleteIngredient(@PathVariable("id") id: Long) =
        ingredientService.deleteIngredient(id)

    @GetMapping("coffees")
    fun getCoffees(): List<Coffee> =
        coffeeJpaRepository.findAll().toList()

    @GetMapping("coffees/{id}")
    fun getCoffee(@PathVariable("id") id: Long): Coffee =
        coffeeJpaRepository.findById(id).orElseThrow {
            EntityNotFoundException(notFound("Coffee", id)) }
}