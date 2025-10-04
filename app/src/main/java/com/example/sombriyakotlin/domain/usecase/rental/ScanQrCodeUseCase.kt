package com.example.sombriyakotlin.domain.usecase.rental

import androidx.camera.core.ImageProxy
import com.example.sombriyakotlin.data.repository.QrRepositoryImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScanQrCodeUseCase @Inject constructor(
    private val repository: QrRepositoryImpl
) {
    fun execute(imageProxy: ImageProxy): Flow<String> = repository.scanQrCodeFlow(imageProxy)
}
