package com.example.sombriyakotlin.data.repository

import android.util.Log
import com.example.sombriyakotlin.data.serviceAdapter.UserApi
import com.example.sombriyakotlin.data.datasource.UserLocalDataSource
import com.example.sombriyakotlin.data.dto.toDomain
import com.example.sombriyakotlin.data.dto.toDto
import com.example.sombriyakotlin.domain.model.CreateUser
import com.example.sombriyakotlin.domain.model.GoogleLogIn
import com.example.sombriyakotlin.domain.model.LogInUser
import com.example.sombriyakotlin.domain.model.User
import com.example.sombriyakotlin.domain.model.UserHistory
import com.example.sombriyakotlin.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val userLocalDataSource: UserLocalDataSource
) : UserRepository {
    override suspend fun createUser(user: CreateUser): User {
//        Log.d("UserRepositoryImpl", "Creando usuario con datos: $user")
        val respuestaUserDto = userApi.createUser(user.toDto())
//        Log.d("UserRepositoryImpl", "Respuesta del servidor: $respuestaUserDto")
        val domainUser = respuestaUserDto.toDomain()
//        Log.d("UserRepositoryImpl", "Usuario registrado: $domainUser")
        userLocalDataSource.saveUser(domainUser)
        return domainUser
    }

    override fun getUser(): Flow<User?> = userLocalDataSource.getUser()

    override suspend fun refreshUserFromRemote(userId: String): User {
        val userDto = userApi.getUser(userId)
        val domainUser = userDto.toDomain()
        userLocalDataSource.saveUser(domainUser)
        return domainUser
    }

    override suspend fun logInUser(credentials: LogInUser): User {
        val respuestaUserDto = userApi.logInUser(credentials.toDto())
        val domainUser = respuestaUserDto.toDomain()
        userLocalDataSource.saveUser(domainUser)
        return domainUser
    }

    override suspend fun userTotalDistance(userId: String): UserHistory {
        val distanceDto = userApi.getTotalDistance(userId)
        val domainDistance = distanceDto.toDomain()
        return domainDistance
    }

    override suspend fun googleLogIn(googleId: GoogleLogIn): User {
        val respuestaUserDto = userApi.googleLogIn(googleId.toDto())
        Log.d("UserRepositoryImpl", "Respuesta del servidor: $respuestaUserDto")
        val domainUser = respuestaUserDto.toDomain()
        Log.d("UserRepositoryImpl", "Usuario registrado (domain): $domainUser")
        userLocalDataSource.saveUser(domainUser)
        return domainUser
    }

    override suspend fun closeSession() {
        userLocalDataSource.clearUser()
    }

}
