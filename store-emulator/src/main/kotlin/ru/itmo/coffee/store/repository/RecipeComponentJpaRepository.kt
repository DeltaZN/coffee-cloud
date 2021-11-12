package ru.itmo.coffee.store.repository

import org.springframework.data.repository.CrudRepository
import ru.itmo.coffee.store.entity.RecipeComponent

interface RecipeComponentJpaRepository : CrudRepository<RecipeComponent, Long>