package com.example.sombriyakotlin.data.repository

import android.util.Log
import com.example.sombriyakotlin.data.dto.ReportDto
import com.example.sombriyakotlin.data.dto.toDomain
import com.example.sombriyakotlin.data.dto.toDto
import com.example.sombriyakotlin.data.serviceAdapter.ReportApi
import com.example.sombriyakotlin.domain.model.Report
import com.example.sombriyakotlin.domain.repository.ReportRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import java.io.IOException
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val reportApi: ReportApi
) : ReportRepository{
    override suspend fun createReport(calificacion:Int, descripcion:String, image:MultipartBody.Part, rentalId:String) {
        withContext(Dispatchers.IO) {
            Log.d("ReportRepository", "createReport: $image $calificacion $descripcion $rentalId")
            try {
                val resp = reportApi.createReport(
                    image = image,
                    someInt = calificacion,
                    description = descripcion,
                    rentalId = rentalId
                )

                Log.d("ReportRepository", "createReport: response raw = $resp")

                if (!resp.isSuccessful) {
                    val err = resp.errorBody()?.string()
                    Log.e("ReportRepository", "Status ${resp.code()} -> $err")
                } else {
                    // Ten en cuenta que .string() consume el body
                    val bodyText = resp.body()?.string() ?: "empty"
                    Log.d("ReportRepository","OK, body: $bodyText")
                }
            } catch (e: CancellationException) {
                // Respetar la cancelación: volver a lanzar para que los callers sepan que fue cancelado
                Log.w("ReportRepository", "createReport: cancelled", e)
                throw e
            } catch (e: IOException) {
                // Errores de red / I/O
                Log.e("ReportRepository", "createReport: IO error", e)
                throw e // o maneja según tu política
            } catch (e: Exception) {
                // Otros errores inesperados
                Log.e("ReportRepository", "createReport: unexpected error", e)
                throw e
            }
        }

    }

    override suspend fun getReportById(reportId: String): Report {
        return reportApi.getRentalById(reportId).toDomain();

    }
}


