package com.example.sombriyakotlin.data.repository

import android.util.Log
import com.example.sombriyakotlin.data.api.RentalApi
import com.example.sombriyakotlin.data.api.UserApi
import com.example.sombriyakotlin.data.dto.toDomain
import com.example.sombriyakotlin.data.dto.toDto
import com.example.sombriyakotlin.data.dto.toEndDto
import com.example.sombriyakotlin.data.dto.toRequestDto
import com.example.sombriyakotlin.domain.model.Rental
import com.example.sombriyakotlin.domain.model.User
import com.example.sombriyakotlin.domain.repository.RentalRepository
import javax.inject.Inject

class RentalRepositoryImpl @Inject constructor(
    private val rentalApi: RentalApi
): RentalRepository{
    override suspend fun createRental(rental: Rental): Rental {
        Log.d("RENT", "YA MEJOR DICHO LO MANDO")
        val response = rentalApi.createRental(rental.toRequestDto())
        Log.d("RENT", "acaaaaaaaaaaaaaaaaaaaaaaa muereeeeeeeeeee")
        return  response.toDomain()

    }

    override suspend fun endRental(rental: Rental): Rental {
        val response = rentalApi.endRental(rental.toEndDto())
        return response.toDomain()
    }

    override suspend fun getRentalsUser(userId: String): List<Rental> {
        return rentalApi.getRentalsByUserAndStatus(userId, "ongoing").map { it.toDomain()
    }

    }

}