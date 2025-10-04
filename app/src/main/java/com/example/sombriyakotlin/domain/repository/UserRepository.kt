package com.example.sombriyakotlin.domain.repository

import com.example.sombriyakotlin.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun createUser(user: User):User
    fun getUser(): Flow<User?>
    suspend fun refreshUserFromRemote(userId: String): User

}
