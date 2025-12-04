package com.example.sombriyakotlin.data.serviceAdapter

import com.example.sombriyakotlin.data.dto.ResponseReportDto
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import okhttp3.ResponseBody
import retrofit2.Response

interface ReportApi {
    @Multipart
    @POST("rental-format")
    suspend fun createReport(
        @Part image: MultipartBody.Part,
        @Part("someInt") someInt: Int,
        @Part("description") description: String,
        @Part("rentalId") rentalId: String
    ) : Response<ResponseBody>
    @GET("rental-format/rental/{id}")
    suspend fun getRentalById(
        @Path("id") id: String
    ): ResponseReportDto


}