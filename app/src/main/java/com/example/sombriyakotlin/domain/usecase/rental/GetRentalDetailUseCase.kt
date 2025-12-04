package com.example.sombriyakotlin.domain.usecase.rental

import com.example.sombriyakotlin.domain.model.Rental
import com.example.sombriyakotlin.domain.repository.RentalRepository
import javax.inject.Inject

class GetRentalDetailUseCase @Inject constructor(
    private val repo: RentalRepository
) {
    suspend operator fun invoke(rentalId: String): Rental {
        return repo.getRentalDetail(rentalId)
    }
}