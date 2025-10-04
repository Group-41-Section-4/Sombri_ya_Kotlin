package com.example.sombriyakotlin.domain.repository

import com.example.sombriyakotlin.domain.model.Rental

interface RentalRepository {
    suspend fun createRental(rental: Rental): Rental
    suspend fun endRental(rental: Rental): Rental
    suspend fun getRentalsUser(userId: String): List<Rental>

}

