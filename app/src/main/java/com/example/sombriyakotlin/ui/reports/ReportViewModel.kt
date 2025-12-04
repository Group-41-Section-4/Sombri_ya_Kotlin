package com.example.sombriyakotlin.ui.reports

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.domain.usecase.rental.RentalUseCases
import com.example.sombriyakotlin.domain.usecase.report.ReportUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.ByteArrayOutputStream

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val reportUseCases: ReportUseCases,
    private val rentalUseCases: RentalUseCases
) : ViewModel() {

    fun sendReport(calificacion:Int, description:String, uri: Uri, context: Context) {
        viewModelScope.launch {
            val rental = rentalUseCases.getCurrentRentalUseCase.invoke()

            val imagePart = withContext(Dispatchers.IO) {
                uriToMultipart(context, uri)  // lectura de Uri → IO
            }
            Log.d("ReportViewModel", "sendReport: $imagePart $calificacion $description ${rental.first()?.id.toString()}")

            withContext(Dispatchers.IO) {
                reportUseCases.createReportUseCase(
                    calificacion,
                    description,
                    imagePart,
                    rental.first()?.id.toString()
                )
            }
        }
    }






    fun uriToMultipart(context: Context, uri: Uri?, name: String = "image.jpg"): MultipartBody.Part? {
        if (uri == null) return null

        val bitmap = context.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it) } ?: return null

        // Redimensionar
        val maxWidth = 500
        val maxHeight = 500
        val ratio = minOf(maxWidth.toFloat() / bitmap.width, maxHeight.toFloat() / bitmap.height, 1f)
        val scaled = Bitmap.createScaledBitmap(bitmap, (bitmap.width * ratio).toInt(), (bitmap.height * ratio).toInt(), true)

        // Comprimir a JPEG
        val stream = ByteArrayOutputStream()
        scaled.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val bytes = stream.toByteArray()

        val requestBody = bytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", name, requestBody)
    }
}