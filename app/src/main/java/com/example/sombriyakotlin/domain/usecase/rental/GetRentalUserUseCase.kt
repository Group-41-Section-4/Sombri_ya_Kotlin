package com.example.sombriyakotlin.domain.usecase.rental

import com.example.sombriyakotlin.domain.model.Rental
import com.example.sombriyakotlin.domain.repository.RentalRepository
import javax.inject.Inject

class GetRentalUserUseCase @Inject constructor(
    private val rentalRepository: RentalRepository
){
    suspend fun invoke(userID: String): List<Rental> {
        return rentalRepository.getRentalsUser(userID)
    }

}