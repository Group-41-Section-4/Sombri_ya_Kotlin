package com.example.sombriyakotlin.domain.model

data class Localization(
    val latitude: Double,
    val longitude: Double
)

data class CreateLocation(
    val latitude: Double,
    val longitude: Double,
    val user_id: String
)
