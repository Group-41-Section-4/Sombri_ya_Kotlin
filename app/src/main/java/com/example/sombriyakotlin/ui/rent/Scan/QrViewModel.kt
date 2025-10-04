package com.example.sombriyakotlin.ui.rent.Scan

import androidx.camera.core.ImageAnalysis
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.domain.usecase.rental.ScanQrCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QrViewModel @Inject constructor(
    private val scanQrCodeUseCase: ScanQrCodeUseCase
) : ViewModel() {

    private val _qrCode = MutableStateFlow<String?>(null)
    val qrCode: StateFlow<String?> = _qrCode

    fun getAnalyzer(): ImageAnalysis.Analyzer {
        return ImageAnalysis.Analyzer { imageProxy ->
            viewModelScope.launch {
                scanQrCodeUseCase.execute(imageProxy).collectLatest { code ->
                    _qrCode.value = code
                }
            }
        }
    }
}
