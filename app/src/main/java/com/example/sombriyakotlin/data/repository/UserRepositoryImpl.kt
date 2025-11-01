package com.example.sombriyakotlin.data.repository

import android.util.Log
import com.example.sombriyakotlin.data.api.UserApi
import com.example.sombriyakotlin.data.datasource.UserLocalDataSource
import com.example.sombriyakotlin.data.dto.toDomain
import com.example.sombriyakotlin.data.dto.toDto
import com.example.sombriyakotlin.domain.model.CreateUser
import com.example.sombriyakotlin.domain.model.LogInUser
import com.example.sombriyakotlin.domain.model.User
import com.example.sombriyakotlin.domain.model.UserHistory
import com.example.sombriyakotlin.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val userLocalDataSource: UserLocalDataSource,
) : UserRepository {
    override suspend fun createUser(user: CreateUser): User =withContext(Dispatchers.IO) {
        Log.d("UserRepositoryImpl", "Creando usuario con datos: $user")
        val respuestaUserDto = userApi.createUser(user.toDto())
        Log.d("UserRepositoryImpl", "Respuesta del servidor: $respuestaUserDto")
        val domainUser = respuestaUserDto.toDomain()
        Log.d("UserRepositoryImpl", "Usuario registrado: $domainUser")
        userLocalDataSource.saveUser(domainUser)
        domainUser
    }

    override fun getUser(): Flow<User?> = userLocalDataSource.getUser()

    override suspend fun refreshUserFromRemote(userId: String): User = withContext(Dispatchers.IO) {
        val userDto = userApi.getUser(userId)
        val domainUser = userDto.toDomain()
        userLocalDataSource.saveUser(domainUser)
        domainUser
    }

    override suspend fun logInUser(credentials: LogInUser): User = withContext(Dispatchers.IO) {
        val respuestaUserDto = userApi.logInUser(credentials.toDto())
        val domainUser = respuestaUserDto.toDomain()
        userLocalDataSource.saveUser(domainUser)
        domainUser
    }

    override suspend fun userTotalDistance(userId: String): UserHistory = withContext(Dispatchers.IO) {
        val distanceDto = userApi.getTotalDistance(userId)
        val domainDistance = distanceDto.toDomain()
        domainDistance
    }

    override suspend fun clearUser() = withContext(Dispatchers.IO) {
        userLocalDataSource.clearUser()
    }

}
