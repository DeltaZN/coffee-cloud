package ru.itmo.coffee.constructor.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
class RecipeComponent(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @ManyToOne
    @JsonIgnore
    val coffee: CoffeeRecipe? = null,
    @ManyToOne(fetch = FetchType.EAGER)
    val ingredient: Ingredient = Ingredient(),
    val quantity: Double = 0.0,
    val insertionOrder: Int = 0,
)