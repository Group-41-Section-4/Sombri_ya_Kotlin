package com.example.sombriyakotlin.data.serviceAdapter

import com.example.sombriyakotlin.data.dto.CreateUserDto
import com.example.sombriyakotlin.data.dto.DistanceDto
import com.example.sombriyakotlin.data.dto.GoogleLogInDto
import com.example.sombriyakotlin.data.dto.LogInDto
import com.example.sombriyakotlin.data.dto.RespuestaLogInDto
import com.example.sombriyakotlin.data.dto.RespuestaUserDto
import com.example.sombriyakotlin.data.dto.UpdateEmailDto
import com.example.sombriyakotlin.data.dto.UpdateNameDto
import com.example.sombriyakotlin.data.dto.UserDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface UserApi {
    @POST("auth/register")
    suspend fun createUser(@Body createUserDto : CreateUserDto): RespuestaUserDto

    @POST("auth/login/password")
    suspend fun logInUser(@Body credentials: LogInDto): RespuestaLogInDto


    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: String): UserDto

    @GET("users/{id}/total-distance")
    suspend fun getTotalDistance(@Path("id") id: String): DistanceDto

    @POST("auth/login/google")
    suspend fun googleLogIn(@Body googleInDto: GoogleLogInDto): RespuestaLogInDto


    // ---------- PATCH DEL BACK ----------

    @PATCH("users/{id}/nombre")
    suspend fun updateUserName(
        @Path("id") id: String,
        @Body body: UpdateNameDto
    ): UserDto

    @PATCH("users/{id}/email")
    suspend fun updateUserEmail(
        @Path("id") id: String,
        @Body body: UpdateEmailDto
    ): UserDto

    @Multipart
    @PATCH("users/{id}/image")
    suspend fun updateUserImage(
        @Path("id") id: String,
        @Part image: MultipartBody.Part
    ): UserDto

    // Usamos /users/me eliminar la cuenta del usuario logueado
    @DELETE("users/me")
    suspend fun deleteMyAccount(): Response<Unit>
}