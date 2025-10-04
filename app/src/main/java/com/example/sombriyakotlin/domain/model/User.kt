package com.example.sombriyakotlin.domain.model


data class User (
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val biometricEnabled: String = "",
    val createdAt: String = "",
)


data class UserHistory (
    val totalDistance: Double = 0.0,

)