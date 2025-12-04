package com.example.sombriyakotlin.domain.usecase.user

import com.example.sombriyakotlin.domain.model.User
import com.example.sombriyakotlin.domain.repository.UserRepository

class UpdateUserNameUseCase(
    private val repo: UserRepository
) {
    suspend operator fun invoke(newName: String): User {
        return repo.updateUserName(newName)
    }
}
