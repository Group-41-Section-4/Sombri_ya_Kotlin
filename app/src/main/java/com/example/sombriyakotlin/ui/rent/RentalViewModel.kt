package com.example.sombriyakotlin.ui.rent

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.domain.model.Rental
import com.example.sombriyakotlin.domain.usecase.rental.CreateRentalUseCase
import com.example.sombriyakotlin.domain.usecase.user.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RentViewModel @Inject constructor(
    private val createRentalUseCase: CreateRentalUseCase,

    private val userUseCases: UserUseCases
) : ViewModel() {

    sealed class RentState {
        object Idle : RentState()
        object Loading : RentState()
        data class Success(val rental: Rental) : RentState()
        data class Error(val message: String) : RentState()
    }

    private val _rentState = MutableStateFlow<RentState>(RentState.Idle)
    val rentState: StateFlow<RentState> = _rentState

    fun reset() {
        _rentState.value = RentState.Idle
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
                Log.d("USER", "Identiifca al usario")
                val now = Date()
                val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val startTimestamp = formatter.format(now)

                val rental = Rental(
                    id = 0,
                    userId = user.id,
                    stationStartId = stationId,
                    paymentMethodId = null,
                    startLat = 6.2442,
                    startLon = -75.5812,
                    authType = "NFC",
                    status = "ACTIVE",
                    startedAt = startTimestamp,
                    endedAt = null
                )
                Log.d("RENT", "ARMO LA RESERVAAAAAAAA")
                val created = createRentalUseCase.invoke(rental)
                Log.d("RENT", "Mando a crear la reserva")
                _rentState.value = RentState.Success(created)

            } catch (e: Exception) {
                _rentState.value = RentState.Error(e.message ?: "Error creando la reserva")
            }
        }
    }
}
