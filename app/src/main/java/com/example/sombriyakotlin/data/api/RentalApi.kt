package com.example.sombriyakotlin.data.api

import com.example.sombriyakotlin.data.dto.RentalDto
import retrofit2.http.Body
import retrofit2.http.POST

interface RentalApi {
    @POST("rental")
    suspend fun createRental(@Body rentDto: RentalDto): RentalDto

}