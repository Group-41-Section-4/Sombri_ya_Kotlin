package com.example.sombriyakotlin.domain.repository

import com.example.sombriyakotlin.domain.model.User

interface UserRepository {
    suspend fun createUsuario(user: User):User
}