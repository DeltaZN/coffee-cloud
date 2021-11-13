package ru.itmo.coffee.store.entity

import java.time.Instant
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
class CoffeeRecipe(
        @Id
        var id: Long = 0,
        var name: String = "",
        var creationTime: Instant = Instant.now(),
        var modificationTime: Instant = Instant.now(),
        @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
        var components: List<RecipeComponent> = mutableListOf(),
        @OneToOne(mappedBy = "recipe")
        var coffee: Coffee? = null
)