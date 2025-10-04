package com.example.sombriyakotlin.domain.usecase.rental

import History
import com.example.sombriyakotlin.domain.model.Rental
import com.example.sombriyakotlin.domain.repository.RentalRepository
import javax.inject.Inject

class getRentalsHystoryUserUseCase @Inject constructor(
    private val rentalRepository: RentalRepository
){
    suspend fun invoke(userID: String,status: String): List<History> {
        return rentalRepository.getRentalsHystoryByUserAndStatus(userID,status)
    }

}