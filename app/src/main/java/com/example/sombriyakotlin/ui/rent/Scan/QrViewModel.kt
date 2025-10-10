package com.example.sombriyakotlin.ui.rent.Scan

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.camera.core.ImageAnalysis
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.domain.model.Rental
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
    private val rentalUseCases: RentalUseCases,
    private val userUseCases: UserUseCases,
    private val scanQrCodeUseCase: ScanQrCodeUseCase
) : ViewModel() {

    // posible herencia o abstrapcopm
    enum class ScanIntent { START, RETURN }
    private val _scanIntent = MutableStateFlow(RentViewModel.ScanIntent.START)
    val scanIntent: StateFlow<RentViewModel.ScanIntent> = _scanIntent

    fun setReturnIntent() { _scanIntent.value = RentViewModel.ScanIntent.RETURN }
    fun setStartIntent()  { _scanIntent.value = RentViewModel.ScanIntent.START }

    private val _qrCode = MutableStateFlow<String?>(null)
    val qrCode: StateFlow<String?> = _qrCode

    private val _rentState = MutableStateFlow<RentState>(RentState.Idle)
    val rentState: StateFlow<RentState> = _rentState

    fun reset() {
        _rentState.value = RentState.Idle
    }

    fun getAnalyzer(): ImageAnalysis.Analyzer {
        return ImageAnalysis.Analyzer { imageProxy ->
            viewModelScope.launch {
                scanQrCodeUseCase.execute(imageProxy).collectLatest { stationId ->
                    _qrCode.value = stationId
                    when (_scanIntent.value) {
                        RentViewModel.ScanIntent.RETURN -> endCurrentRental(stationId)
                        RentViewModel.ScanIntent.START  -> createReservation(stationId)
                    }
                }
            }
        }
    }

    fun createReservation(stationId: String) {
        Log.d("RENT", "Se empieza a crear la renta")
        viewModelScope.launch {
            _rentState.value = RentState.Loading

            try {

                val user = userUseCases.getUserUseCase().first()
                if (user == null) {
                    _rentState.value = RentState.Error("Usuario no autenticado")
                    return@launch
                }


                Log.d("RENT", "Identiifca al usario")
                val now = Date()
                val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val startTimestamp = formatter.format(now)

                val rental = Rental(
                    userId = user.id,
                    stationStartId = stationId,
                    authType = "qr",
                )
                Log.d("PASO", "ya a punto")
                val created = rentalUseCases.createRentalUseCase.invoke(rental)
                Log.d("PASO", "creada")
                _rentState.value = RentState.Success(created)
                Log.d("PASO", "guardada")


            } catch (e: Exception) {
                val errorMessage = if (e is HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    "Error ${e.code()}: ${errorBody ?: "Solicitud inválida"}"
                } else {
                    e.message ?: "Error desconocido"
                }
                _rentState.value = RentState.Error(errorMessage)
            }
        }
    }

    fun endCurrentRental(stationId: String) {
        viewModelScope.launch {
            _rentState.value = RentState.Loading

            try {
                val current = rentalUseCases.getCurrentRentalUseCase().first()
                if (current == null) {
                    _rentState.value = RentState.Error("No hay un alquiler activo")
                    return@launch
                }
                val user = userUseCases.getUserUseCase().first()
                if (user == null) {
                    _rentState.value = RentState.Error("Usuario no autenticado")
                    return@launch
                }
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val endDate = sdf.format(Date())

                val rentalToEnd = current.copy(
                    userId = user.id,
                    stationStartId = stationId,
                    endedAt = endDate,
                    status = "completed"
                )

                val ended = rentalUseCases.endRentalUseCase.invoke(rentalToEnd)

                _rentState.value = RentState.Success(ended)

            } catch (e: Exception) {
                val msg = if (e is retrofit2.HttpException) {
                    val body = e.response()?.errorBody()?.string()
                    "Error ${e.code()}: ${body ?: "Solicitud inválida"}"
                } else {
                    e.message ?: "Error desconocido"
                }
                _rentState.value = RentState.Error(msg)
            }
        }
    }


}