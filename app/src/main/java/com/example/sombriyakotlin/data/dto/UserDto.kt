package com.example.sombriyakotlin.data.dto

import com.example.sombriyakotlin.domain.model.CreateUser
import com.example.sombriyakotlin.domain.model.LogInUser
import com.example.sombriyakotlin.domain.model.User
import com.example.sombriyakotlin.domain.model.UserHistory

data class CreateUserDto(
    val name: String,
    val email: String,
    val password: String,
    val biometricEnabled: Boolean,
)

data class DistanceDto(
    val totalDistanceKm: Double
)

data class RespuestaUserDto(
    val message: String,
    val user: UserDto
)

data class RespuestaLogInDto(
    val accessToken: String,
    val user: UserDto
)
data class LogInDto(
    val email: String,
    val password: String,
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
fun CreateUser.toDto(): CreateUserDto = CreateUserDto(name, email, password, biometricEnabled)
fun RespuestaUserDto.toDomain(): User = user.toDomain()
fun DistanceDto.toDomain(): UserHistory = UserHistory(totalDistanceKm)

fun RespuestaLogInDto.toDomain(): User = user.toDomain()
fun LogInUser.toDto() : LogInDto = LogInDto(email,password)