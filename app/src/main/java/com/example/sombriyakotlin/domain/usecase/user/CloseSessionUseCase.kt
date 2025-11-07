package com.example.sombriyakotlin.domain.usecase.user

import com.example.sombriyakotlin.domain.repository.UserRepository
import javax.inject.Inject

class CloseSessionUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() {
        userRepository.closeSession()
    }
}