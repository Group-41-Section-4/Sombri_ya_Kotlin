package com.example.sombriyakotlin.data.repository

import com.example.sombriyakotlin.data.api.RentalApi
import com.example.sombriyakotlin.data.api.UserApi
import com.example.sombriyakotlin.data.dto.toDomain
import com.example.sombriyakotlin.data.dto.toDto
import com.example.sombriyakotlin.domain.model.Rental
import com.example.sombriyakotlin.domain.model.User
import com.example.sombriyakotlin.domain.repository.RentalRepository
import javax.inject.Inject

class RentalRepositoryImpl @Inject constructor(
    private val rentalApi: RentalApi
): RentalRepository{
    override suspend fun createRental(rental: Rental): Rental {
        val response = rentalApi.createRental(rental.toDto())
        return  response.toDomain()

}
}