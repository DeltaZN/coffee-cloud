package ru.itmo.coffee

data class MessageResponse(
    val message: String,
)

data class MessageWithIdResponse(
    val message: String,
    val id: Long,
)