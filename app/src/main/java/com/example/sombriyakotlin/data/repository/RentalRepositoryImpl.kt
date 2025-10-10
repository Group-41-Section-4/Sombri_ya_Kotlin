package com.example.sombriyakotlin.data.repository

import History
import android.util.Log
import com.example.sombriyakotlin.data.api.RentalApi
import com.example.sombriyakotlin.data.api.UserApi
import com.example.sombriyakotlin.data.datasource.RentalLocalDataSource
import com.example.sombriyakotlin.data.dto.RentalHistoryDto
import com.example.sombriyakotlin.data.dto.toDomain
import com.example.sombriyakotlin.data.dto.toDto
import com.example.sombriyakotlin.data.dto.toEndDto
import com.example.sombriyakotlin.data.dto.toRequestDto
import com.example.sombriyakotlin.domain.model.Rental
import com.example.sombriyakotlin.domain.model.User
import com.example.sombriyakotlin.domain.repository.RentalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RentalRepositoryImpl @Inject constructor(
    private val rentalApi: RentalApi,
    private val local: RentalLocalDataSource
): RentalRepository{
    override suspend fun createRental(rental: Rental): Rental {

        val response = rentalApi.createRental(rental.toRequestDto())


        val domain = response.toDomain()
        local.saveCurrent(domain)           // ⬅️ Guarda localmente la renta “activa”
        return domain
        }



    override suspend fun endRental(rental: Rental): Rental {
        local.clear()
        Log.d("RENT", "Previo Response: $rental")
        val response = rentalApi.endRental(rental.toEndDto())
        Log.d("RENT", "Despues Response: $response")
        val domain = response.toDomain()
        local.clear()
        return domain
    }

    override suspend fun getRentalsUser(userId: String,status: String): List<Rental> {
        return rentalApi.getRentalsByUserAndStatus(userId, status).map { it.toDomain()}

    }
    override fun currentRental(): Flow<Rental?> = local.getCurrent()

    override suspend fun getRentalsHystoryByUserAndStatus(userId: String, status: String): List<History>{
        return rentalApi.getRentalsHystoryByUserAndStatus(userId, status).map { it.toDomain() }
    }

    override suspend fun clearCurrentRental() {
        local.clear()
    }


}