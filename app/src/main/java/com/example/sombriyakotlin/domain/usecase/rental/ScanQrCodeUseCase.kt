package com.example.sombriyakotlin.domain.usecase.rental

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.example.sombriyakotlin.data.repository.QrRepositoryImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScanQrCodeUseCase @Inject constructor(
    private val repository: QrRepositoryImpl
) {
    @OptIn(ExperimentalGetImage::class)
    fun execute(imageProxy: ImageProxy): Flow<String> = repository.scanQrCodeFlow(imageProxy)
}
