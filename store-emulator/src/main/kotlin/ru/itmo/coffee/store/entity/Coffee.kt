package ru.itmo.coffee.store.entity

import javax.persistence.*

enum class CoffeeType {
    USER, STANDARD
}

@Entity
class Coffee(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    var name: String = "",
    var cost: Double = 0.0,
    var type: CoffeeType = CoffeeType.STANDARD,
    @OneToOne(cascade = [CascadeType.ALL])
    var recipe: CoffeeRecipe? = null,
)