package ru.itmo.coffee.store.entity

import java.time.ZonedDateTime
import javax.persistence.*

@Entity
class CoffeeRecipe(
        @Id
        var id: Long = 0,
        var name: String = "",
        var creationTime: ZonedDateTime = ZonedDateTime.now(),
        var modificationTime: ZonedDateTime = ZonedDateTime.now(),
        @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
        var components: List<RecipeComponent> = mutableListOf(),
        @OneToOne(mappedBy = "recipe")
        var coffee: Coffee? = null
)