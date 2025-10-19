package com.example.sombriyakotlin.ui.rent

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageAnalysis
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.domain.model.Rental
import com.example.sombriyakotlin.domain.usecase.rental.ParseQrDataUseCase
import com.example.sombriyakotlin.domain.usecase.rental.RentalUseCases
import com.example.sombriyakotlin.domain.usecase.rental.ScanQrCodeUseCase
import com.example.sombriyakotlin.domain.usecase.user.UserUseCases
import com.example.sombriyakotlin.ui.rent.RentViewModel
import com.example.sombriyakotlin.ui.rent.RentViewModel.RentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class QrViewModel @Inject constructor(
    private val scanQrCodeUseCase: ScanQrCodeUseCase,
    private val parseQrDataUseCase: ParseQrDataUseCase,
) : ViewModel() {

    private val _qrCode = MutableStateFlow<String?>(null)
    val qrCode: StateFlow<String?> = _qrCode


    fun getAnalyzer(): ImageAnalysis.Analyzer {
        return ImageAnalysis.Analyzer { imageProxy ->
            viewModelScope.launch {
                scanQrCodeUseCase.execute(imageProxy).collectLatest { stationJson ->
                    val stationId = parseQrDataUseCase.execute(stationJson)?.station_id
                    if (_qrCode.value == null && stationId != null) {
                        _qrCode.value = stationId
                    }

                }
            }
        }
    }
}