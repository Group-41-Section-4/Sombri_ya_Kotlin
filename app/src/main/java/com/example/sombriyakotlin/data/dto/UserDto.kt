package com.example.sombriyakotlin.data.dto

import com.example.sombriyakotlin.domain.model.User

data class CreateUserDto(
    val name: String,
    val email: String
)

data class UserDto(
    val id: String,
    val name: String,
    val email: String
)

fun UserDto.toDomain(): User = User(id, name, email)
fun User.toDto(): CreateUserDto = CreateUserDto(name, email)
