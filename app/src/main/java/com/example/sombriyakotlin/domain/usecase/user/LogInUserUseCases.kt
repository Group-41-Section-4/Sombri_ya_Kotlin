package com.example.sombriyakotlin.domain.usecase.user

import com.example.sombriyakotlin.domain.model.LogInUser
import com.example.sombriyakotlin.domain.model.User
import com.example.sombriyakotlin.domain.repository.UserRepository
import javax.inject.Inject

class LogInUserUseCases @Inject constructor(private val repo: UserRepository) {
    suspend operator fun invoke(credentials: LogInUser): User = repo.logInUser(credentials)
}