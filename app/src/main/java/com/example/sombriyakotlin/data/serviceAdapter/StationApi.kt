package com.example.sombriyakotlin.data.serviceAdapter

import com.example.sombriyakotlin.data.dto.QueryLocalizationDto
import com.example.sombriyakotlin.data.dto.StationDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface StationApi {
    @POST("stations/nearby")
    suspend fun getStations(@Body queryStationDto: QueryLocalizationDto): Response<List<StationDto>>

    @GET("tags/{uid}/station")
    suspend fun getStationByTag(
        @retrofit2.http.Path("uid") uid: String
    ): StationDto
}