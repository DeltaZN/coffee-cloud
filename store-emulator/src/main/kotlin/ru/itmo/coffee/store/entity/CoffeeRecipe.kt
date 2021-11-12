package ru.itmo.coffee.store.entity

import java.time.ZonedDateTime
import javax.persistence.*

@Entity
class CoffeeRecipe(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,
        var name: String = "",
        var creationTime: ZonedDateTime = ZonedDateTime.now(),
        var modificationTime: ZonedDateTime = ZonedDateTime.now(),
        @OneToMany(fetch = FetchType.LAZY)
        var components: List<RecipeComponent> = mutableListOf(),
)