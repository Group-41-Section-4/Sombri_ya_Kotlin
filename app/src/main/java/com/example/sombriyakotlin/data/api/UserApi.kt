package com.example.sombriyakotlin.data.api

import com.example.sombriyakotlin.data.dto.CreateUserDto
import com.example.sombriyakotlin.data.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("users")
    suspend fun createUser(@Body createUserDto : CreateUserDto): UserDto

}