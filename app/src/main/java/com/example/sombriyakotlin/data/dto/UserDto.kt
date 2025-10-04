package com.example.sombriyakotlin.data.dto

import com.example.sombriyakotlin.domain.model.User
import com.example.sombriyakotlin.domain.model.UserHistory

data class CreateUserDto(
    val name: String,
    val email: String
)

data class DistanceDto(
    val totalDistanceKm: Double
)


data class UserDto(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val biometric_enabled: String,
    val created_at: String,
)

fun UserDto.toDomain(): User = User(id, name, email, password, biometric_enabled, created_at)
fun User.toDto(): CreateUserDto = CreateUserDto(name, email)

fun DistanceDto.toDomain(): UserHistory = UserHistory(totalDistanceKm)
