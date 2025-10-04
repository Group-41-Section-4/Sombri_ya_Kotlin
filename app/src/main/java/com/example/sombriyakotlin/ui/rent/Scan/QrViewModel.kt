package com.example.sombriyakotlin.ui.rent.Scan

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.domain.model.Rental
import com.example.sombriyakotlin.domain.usecase.rental.RentalUseCases
import com.example.sombriyakotlin.domain.usecase.rental.ScanQrCodeUseCase
import com.example.sombriyakotlin.domain.usecase.user.UserUseCases
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
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class QrViewModel @Inject constructor(
    private val rentalUseCases: RentalUseCases,
    private val userUseCases: UserUseCases,
    private val scanQrCodeUseCase: ScanQrCodeUseCase
) : ViewModel() {

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
                scanQrCodeUseCase.execute(imageProxy).collectLatest { code ->
                    _qrCode.value = code
                    createReservation("acadc4ef-f5b3-4ab8-9ab5-58f1161f0799")
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
                Log.d("RENT", "ARMO LA RESERVAAAAAAAA")
                val created = rentalUseCases.createRentalUseCase.invoke(rental)
                Log.d("RENT", "Mando a crear la reserva")
                _rentState.value = RentState.Success(created)
                val closed = rentalUseCases.endRentalUseCase.invoke(rental)
                Log.d("RENT", "Mando a cerrar la reserva")
                _rentState.value = RentState.Success(closed)


            } catch (e: Exception) {
                val errorMessage = if (e is HttpException) {
                    try {
                        val errorBody = e.response()?.errorBody()?.string()
                        if (errorBody != null) {
                            val jsonObject = JSONObject(errorBody)
                            jsonObject.getString("message")
                        } else {
                            "Error ${e.code()}: Respuesta de error vacía"
                        }
                    } catch (jsonE: Exception) {
                        "Error al parsear la respuesta de error"
                    }
                } else {
                    e.message ?: "Error creando la reserva"
                }
                _rentState.value = RentState.Error(errorMessage)
                Log.e("RENT", "Error al crear la reserva ${errorMessage}", e)
            }
        }
    }


}