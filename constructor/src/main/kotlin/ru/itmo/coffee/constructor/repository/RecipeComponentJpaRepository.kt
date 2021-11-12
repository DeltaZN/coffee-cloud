package ru.itmo.coffee.constructor.repository

import org.springframework.data.repository.CrudRepository
import ru.itmo.coffee.constructor.entity.RecipeComponent

interface RecipeComponentJpaRepository : CrudRepository<RecipeComponent, Long>