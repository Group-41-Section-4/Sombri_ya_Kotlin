package com.example.sombriyakotlin.domain.usecase.report

import com.example.sombriyakotlin.domain.repository.ReportRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class CreateReportUseCase @Inject constructor(
    private val repository: ReportRepository
){
    suspend operator fun invoke(calificacion: Int, descripcion: String, image: MultipartBody.Part?, rentalId: String) = repository.createReport(calificacion, descripcion, image?: MultipartBody.Part.createFormData("image", ""), rentalId)
}
