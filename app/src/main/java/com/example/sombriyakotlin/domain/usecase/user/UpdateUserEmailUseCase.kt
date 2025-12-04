package com.example.sombriyakotlin.domain.usecase.user

import com.example.sombriyakotlin.domain.model.User
import com.example.sombriyakotlin.domain.repository.UserRepository

class UpdateUserEmailUseCase(
    private val repo: UserRepository
) {
    suspend operator fun invoke(newEmail: String): User {
        return repo.updateUserEmail(newEmail)
    }
}
