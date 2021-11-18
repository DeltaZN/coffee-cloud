package ru.itmo.coffee.store.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.Instant
import javax.persistence.*

@Entity
class CoffeeRecipe(
        @Id
        var id: Long = 0,
        var name: String = "",
        var creationTime: Instant = Instant.now(),
        var modificationTime: Instant = Instant.now(),
        @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
        var components: List<RecipeComponent> = mutableListOf(),
        @OneToOne(mappedBy = "recipe")
        @JsonIgnore
        var coffee: Coffee? = null
)