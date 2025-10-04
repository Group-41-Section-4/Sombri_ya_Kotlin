package com.example.sombriyakotlin.data.repository

import com.example.sombriyakotlin.data.api.UserApi
import com.example.sombriyakotlin.data.datasource.UserLocalDataSource
import com.example.sombriyakotlin.data.dto.toDomain
import com.example.sombriyakotlin.data.dto.toDto
import com.example.sombriyakotlin.domain.model.User
import com.example.sombriyakotlin.domain.model.UserHistory
import com.example.sombriyakotlin.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val userLocalDataSource: UserLocalDataSource
) : UserRepository {
    override suspend fun createUser(user: User): User {
        val userDto = userApi.createUser(user.toDto())
        val domainUser = userDto.toDomain()
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

    override suspend fun userTotalDistance(userId: String): UserHistory {
        val distanceDto = userApi.getTotalDistance(userId)
        val domainDistance = distanceDto.toDomain()
        return domainDistance
    }

}
