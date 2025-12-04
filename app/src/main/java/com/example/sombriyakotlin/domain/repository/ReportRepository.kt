package com.example.sombriyakotlin.domain.repository

import com.example.sombriyakotlin.domain.model.Report
import okhttp3.MultipartBody

interface ReportRepository {
    suspend fun createReport(calificacion:Int, descripcion:String, image:MultipartBody.Part, rentalId:String)
    suspend fun getReportById(reportId: String): Report
}
