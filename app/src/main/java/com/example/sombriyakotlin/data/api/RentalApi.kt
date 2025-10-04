package com.example.sombriyakotlin.data.api

import retrofit2.http.Body
import retrofit2.http.POST

interface RentApi {
    @POS    T("rent")
    suspend fun createRent(@Body rentDto: RentDto): RentDto

}