package ru.itmo.coffee.constructor.entity

import javax.persistence.*

@Entity
class Ingredient(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    val name: String = "",
    val cost: Double = 0.0,
    @Column(name = "volume_ml", nullable = false)
    val volumeMl: Double = 0.0
)