package com.example.sombriyakotlin.data.dto

import com.example.sombriyakotlin.domain.model.User

data class UserDto(
    val id: Int,
    val name: String,
    val email: String
)

fun UserDto.toDomain(): User = User(id, name, email)
fun User.toDto(): UserDto = UserDto(id, name, email)
