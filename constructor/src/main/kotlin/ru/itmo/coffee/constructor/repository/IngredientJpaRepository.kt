package ru.itmo.coffee.constructor.repository

import org.springframework.data.repository.CrudRepository
import ru.itmo.coffee.constructor.entity.Ingredient

interface IngredientJpaRepository : CrudRepository<Ingredient, Long>