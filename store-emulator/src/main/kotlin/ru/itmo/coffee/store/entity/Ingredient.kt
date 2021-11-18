package ru.itmo.coffee.store.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
class Ingredient(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    var name: String = "",
    var cost: Double = 0.0,
    var calories: Double = 0.0,
    @Column(name = "volume_ml", nullable = false)
    var volumeMl: Double = 0.0,
    @OneToMany(mappedBy = "ingredient")
    @JsonIgnore
    var recipeComponents: MutableList<RecipeComponent> = mutableListOf(),
)