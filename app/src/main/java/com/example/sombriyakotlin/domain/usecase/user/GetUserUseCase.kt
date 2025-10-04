package com.example.sombriyakotlin.domain.usecase.user

import com.example.sombriyakotlin.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val repo: UserRepository) {
    operator fun invoke() = repo.getUser()
}