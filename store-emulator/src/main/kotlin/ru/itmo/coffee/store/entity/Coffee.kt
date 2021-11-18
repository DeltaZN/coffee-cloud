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
    @JoinTable(name = "emp_workstation",
        joinColumns = [JoinColumn(name = "coffee_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "coffee_recipe_id", referencedColumnName = "id")])
    var recipe: CoffeeRecipe? = null,
)