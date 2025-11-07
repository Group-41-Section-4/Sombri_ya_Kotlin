package com.example.sombriyakotlin.domain.repository

import History
import com.example.sombriyakotlin.domain.model.Rental
import kotlinx.coroutines.flow.Flow

interface RentalRepository {
    suspend fun createRental(rental: Rental): Rental
    suspend fun endRental(rental: Rental): Rental
    suspend fun getRentalsUser(userId: String,state: String): List<Rental>

    // “renta actual” guardada localmente
    fun currentRental(): Flow<Rental?>

    suspend fun clearCurrentRental()

    suspend fun getRentalsHystoryByUserAndStatus(userId: String,state: String): List<History>
    suspend fun setRentalCurrent(rental:Rental)

    suspend fun getActiveRentalRemote(userId: String): Rental?



}

