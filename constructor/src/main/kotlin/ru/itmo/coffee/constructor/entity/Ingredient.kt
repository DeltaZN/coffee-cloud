package ru.itmo.coffee.constructor.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
class Ingredient(
    @Id
    var id: Long = 0,
    var name: String = "",
    var cost: Double = 0.0,
    var calories: Double = 0.0,
    @Column(name = "volume_ml", nullable = false)
    var volumeMl: Double = 0.0,
    @OneToMany(mappedBy = "ingredient", fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JsonIgnore
    var recipeComponents: MutableList<RecipeComponent> = mutableListOf(),
)