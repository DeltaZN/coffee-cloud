package ru.itmo.coffee.constructor.entity

import java.time.Instant
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
class CoffeeRecipe(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,
        var name: String = "",
        var creationTime: Instant = Instant.now(),
        var modificationTime: Instant = Instant.now(),
        @OneToMany(fetch = FetchType.EAGER, mappedBy = "coffee")
        var components: List<RecipeComponent> = mutableListOf(),
)