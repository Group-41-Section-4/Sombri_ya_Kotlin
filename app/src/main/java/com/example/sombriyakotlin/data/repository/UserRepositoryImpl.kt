package com.example.sombriyakotlin.data.repository
import com.example.sombriyakotlin.data.api.UserApi
import com.example.sombriyakotlin.domain.model.User
import com.example.sombriyakotlin.data.dto.toDto
import com.example.sombriyakotlin.data.dto.toDomain

import com.example.sombriyakotlin.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi
): UserRepository {

    override suspend fun createUsuario(user: User): User {
        val response = userApi.createUser(user.toDto())
        return  response.toDomain()
    }

}
