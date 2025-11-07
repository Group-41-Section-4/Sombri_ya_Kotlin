package com.example.sombriyakotlin.domain.usecase.rental

import com.example.sombriyakotlin.domain.model.Rental
import com.example.sombriyakotlin.domain.repository.RentalRepository
import javax.inject.Inject

class GetActiveRentalRemoteUseCase @Inject constructor (
    private val repository: RentalRepository
    ) {
        suspend operator fun invoke(userId: String): Rental? {
            return repository.getActiveRentalRemote(userId)
        }
    }