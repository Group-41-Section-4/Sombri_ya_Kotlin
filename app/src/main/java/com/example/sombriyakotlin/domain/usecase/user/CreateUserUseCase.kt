package com.example.sombriyakotlin.domain.usecase.user

import com.example.sombriyakotlin.domain.model.CreateUser
import com.example.sombriyakotlin.domain.model.User
import com.example.sombriyakotlin.domain.repository.UserRepository
import javax.inject.Inject

class CreateUserUseCase @Inject constructor (
    private val userRepository: UserRepository
) {
    suspend fun invoke(user: CreateUser): User {
        return userRepository.createUser(user)
    }
}