package com.example.sombriyakotlin.data.serviceAdapter

import com.example.sombriyakotlin.data.dto.MessageDto
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatbotApi {
    @POST("ask")
    suspend fun ask(@Body messageDto: MessageDto ): MessageDto

    @POST("stream")
    suspend fun stream(@Body messageDto: MessageDto ): MessageDto
}