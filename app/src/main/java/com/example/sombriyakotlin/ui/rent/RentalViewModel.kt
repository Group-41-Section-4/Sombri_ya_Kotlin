package com.example.sombriyakotlin.feature.rent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.domain.model.Rental
import com.example.sombriyakotlin.domain.usecase.rental.CreateRentalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RentViewModel @Inject constructor(
    private val createRentalUseCase: CreateRentalUseCase
) : ViewModel() {

    sealed class RentState {
        object Idle : RentState()
        object Scanning : RentState()
        object Starting : RentState()
        data class Success(val rent: Rental) : RentState()
        data class Error(val message: String) : RentState()
    }

    private val _state = MutableStateFlow<RentState>(RentState.Idle)
    val state: StateFlow<RentState> = _state

    /**
     * Llamar esto cuando el NFC detecte una etiqueta.
     * @param userId            UUID del usuario (según backend)
     * @param stationStartId    Tomado del tag NFC (o mapeado a partir del texto del tag)
     * @param startLat/lon      GPS actual (requerido por backend)
     * @param paymentMethodId   Opcional
     */
    fun startRental(
        userId: String,
        stationStartId: String,
        startLat: Double,
        startLon: Double,
        paymentMethodId: String? = null
    ) {
        viewModelScope.launch {
            _state.value = RentState.Starting
            /*
            try {

                val created = createRentalUseCase.invoke(
                    userId = userId,
                    stationStartId = stationStartId,
                    startLat = startLat,
                    startLon = startLon,
                    authType = "NFC",
                    paymentMethodId = paymentMethodId
                )
                _state.value = RentState.Success(created)
            } catch (e: Exception) {
                _state.value = RentState.Error(e.message ?: "Error iniciando renta")
            }*/
                //
        }
    }

    fun setScanning() { _state.value = RentState.Scanning }
    fun reset() { _state.value = RentState.Idle }
}
