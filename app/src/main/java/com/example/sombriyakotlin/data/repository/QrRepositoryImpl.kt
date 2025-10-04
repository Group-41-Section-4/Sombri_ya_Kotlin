package com.example.sombriyakotlin.data.repository

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class QrRepositoryImpl @Inject constructor() {

    @ExperimentalGetImage
    fun scanQrCodeFlow(imageProxy: ImageProxy): Flow<String> = callbackFlow {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val scanner = BarcodeScanning.getClient()

            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    barcodes.firstOrNull()?.rawValue?.let(::trySend)
                }
                .addOnFailureListener {
                    close(it)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                    close()
                }
        } else {
            imageProxy.close()
            close()
        }
        awaitClose {  }
    }
}
