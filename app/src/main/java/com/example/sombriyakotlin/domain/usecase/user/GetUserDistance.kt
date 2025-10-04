package com.example.sombriyakotlin.domain.usecase.user

import com.example.sombriyakotlin.domain.repository.UserRepository
import javax.inject.Inject

class GetUserDistance  @Inject constructor(
    private val repo: UserRepository
) {
    suspend fun invoke(userId: String) = repo.userTotalDistance(userId)
}