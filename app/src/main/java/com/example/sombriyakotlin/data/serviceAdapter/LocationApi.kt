package com.example.sombriyakotlin.data.serviceAdapter

import com.example.sombriyakotlin.data.dto.CreateLocationDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LocationApi {

    @POST("locations")
    suspend fun createRental(@Body createLocationDto: CreateLocationDto): Response<ResponseBody>

}