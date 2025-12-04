package com.example.sombriyakotlin.domain.usecase.user

import com.example.sombriyakotlin.domain.repository.UserRepository

class DeleteMyAccountUseCase(
    private val repo: UserRepository
) {
    suspend operator fun invoke() {
        repo.deleteMyAccount()
    }
}
