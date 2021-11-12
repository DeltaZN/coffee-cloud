package ru.itmo.coffee.constructor.entity

import javax.persistence.*

@Entity
class RecipeComponent(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @ManyToOne
    val coffee: CoffeeRecipe? = null,
    @ManyToOne
    val ingredient: Ingredient = Ingredient(),
    val quantity: Double = 0.0,
    @Column(name = "insertion_order")
    val insertionOrder: Int = 0,
)