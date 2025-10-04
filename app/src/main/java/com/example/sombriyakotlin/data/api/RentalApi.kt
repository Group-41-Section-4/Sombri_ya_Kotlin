package com.example.sombriyakotlin.data.api

import com.example.sombriyakotlin.data.dto.EndRentalDto
import com.example.sombriyakotlin.data.dto.RentalDto
import com.example.sombriyakotlin.data.dto.RentalRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface RentalApi {
    @POST("rentals/start")
    suspend fun createRental(@Body rentDto: RentalRequestDto): RentalDto

    @POST("rentals/end")
    suspend fun endRental(@Body rentDto: EndRentalDto): RentalDto

}