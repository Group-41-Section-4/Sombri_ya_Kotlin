package com.example.sombriyakotlin.data.api

import com.example.sombriyakotlin.data.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("user")
    suspend fun createUser(@Body usuariodto: UserDto): UserDto

}