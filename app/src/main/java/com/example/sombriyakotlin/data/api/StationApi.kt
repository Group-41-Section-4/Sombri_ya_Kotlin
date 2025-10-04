package com.example.sombriyakotlin.data.api

import com.example.sombriyakotlin.data.dto.QueryStationDto
import com.example.sombriyakotlin.data.dto.StationDto
import retrofit2.http.Body
import retrofit2.http.GET

interface StationApi {
    @GET("stations")
    suspend fun getStations(@Body queryStationDto: QueryStationDto): List<StationDto>

}