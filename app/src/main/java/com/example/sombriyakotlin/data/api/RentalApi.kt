package com.example.sombriyakotlin.data.api

import com.example.sombriyakotlin.data.dto.RentalDto
import com.example.sombriyakotlin.data.dto.RentalRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface RentalApi {
    @POST("rentals/start")
    suspend fun createRental(@Body rentDto: RentalRequest): RentalDto

}