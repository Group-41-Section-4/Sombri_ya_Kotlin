package com.example.sombriyakotlin.domain.usecase.user

import com.example.sombriyakotlin.domain.model.User
import com.example.sombriyakotlin.domain.repository.UserRepository
import javax.inject.Inject

class RefreshUserUseCase @Inject constructor(private val repo: UserRepository) {
    suspend operator fun invoke(userId: String): User = repo.refreshUserFromRemote(userId)
}
