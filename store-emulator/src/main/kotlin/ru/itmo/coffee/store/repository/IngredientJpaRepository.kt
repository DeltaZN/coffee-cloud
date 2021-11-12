package ru.itmo.coffee.store.repository

import org.springframework.data.repository.CrudRepository
import ru.itmo.coffee.store.entity.Ingredient

interface IngredientJpaRepository : CrudRepository<Ingredient, Long>