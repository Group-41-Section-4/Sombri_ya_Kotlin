package com.example.sombriyakotlin.domain.repository

import com.example.sombriyakotlin.domain.model.CreateUser
import com.example.sombriyakotlin.domain.model.LogInUser
import com.example.sombriyakotlin.domain.model.User
import com.example.sombriyakotlin.domain.model.UserHistory
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun createUser(user: CreateUser):User
    fun getUser(): Flow<User?>
    suspend fun refreshUserFromRemote(userId: String): User

    suspend fun logInUser(credentials: LogInUser): User
    suspend fun userTotalDistance(userId: String): UserHistory

}
