package ru.itmo.coffee.store.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import ru.itmo.coffee.store.entity.CoffeeRecipe
import ru.itmo.coffee.store.entity.Ingredient
import javax.persistence.*

@Entity
class RecipeComponent(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @ManyToOne
    @JsonIgnore
    var recipe: CoffeeRecipe? = null,
    @ManyToOne
    var ingredient: Ingredient? = Ingredient(),
    val quantity: Double = 0.0,
    @Column(name = "insertion_order")
    val insertionOrder: Int = 0,
)