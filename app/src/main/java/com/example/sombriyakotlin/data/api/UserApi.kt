package com.example.sombriyakotlin.data.api

import com.example.sombriyakotlin.data.dto.CreateUserDto
import com.example.sombriyakotlin.data.dto.DistanceDto
import com.example.sombriyakotlin.data.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {
    @POST("users")
    suspend fun createUser(@Body createUserDto : CreateUserDto): UserDto

    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: String): UserDto

    @GET("users/{id}/total-distance")
    suspend fun getTotalDistance(@Path("id") id: String): DistanceDto

}