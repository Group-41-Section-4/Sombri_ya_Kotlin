package com.example.sombriyakotlin.data.serviceAdapter

import com.example.sombriyakotlin.data.dto.EndRentalDto
import com.example.sombriyakotlin.data.dto.RentalDto
import com.example.sombriyakotlin.data.dto.RentalHistoryDto
import com.example.sombriyakotlin.data.dto.RentalRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RentalApi {
    @POST("rentals/start")
    suspend fun createRental(@Body rentDto: RentalRequestDto): RentalDto

    @POST("rentals/end")
    suspend fun endRental(@Body rentDto: EndRentalDto): RentalDto

    @GET("rentals")
    suspend fun getRentalsByUserAndStatus(
        @Query("user_id") userId: String,
        @Query("status") status: String
    ): List<RentalDto>

    @GET("rentals")
    suspend fun getRentalsHystoryByUserAndStatus(
        @Query("user_id") userId: String,
        @Query("status") status: String
    ): List<RentalHistoryDto>

    @GET("rentals")
    suspend fun getOneRental(@retrofit2.http.Path("id") id: String): RentalDto

}