package com.example.sombriyakotlin.domain.usecase.rental

import com.example.sombriyakotlin.domain.model.Rental
import com.example.sombriyakotlin.domain.repository.RentalRepository
import javax.inject.Inject

class EndRentalUseCase @Inject constructor(
    private val rentalRepository: RentalRepository
) {
    suspend fun invoke(rental: Rental): Rental {
        return rentalRepository.endRental(rental)
    }


}