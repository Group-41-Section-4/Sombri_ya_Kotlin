package com.example.sombriyakotlin.ui.rent

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.domain.model.Rental
import com.example.sombriyakotlin.domain.usecase.rental.RentalUseCases
import com.example.sombriyakotlin.domain.usecase.user.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class RentViewModel @Inject constructor(
    private val rentalUseCases: RentalUseCases,
    private val userUseCases: UserUseCases
) : ViewModel() {

    sealed class RentState {
        object Idle : RentState()
        object Loading : RentState()
        data class Success(val rental: Rental) : RentState()
        data class Error(val message: String) : RentState()
    }
    enum class ScanIntent { START, RETURN }


    private val _rentState = MutableStateFlow<RentState>(RentState.Idle)
    val rentState: StateFlow<RentState> = _rentState

    private val _scanIntent = MutableStateFlow(ScanIntent.START)
    val scanIntent: StateFlow<ScanIntent> = _scanIntent

    fun setReturnIntent() { _scanIntent.value = ScanIntent.RETURN }
    fun setStartIntent()  { _scanIntent.value = ScanIntent.START }


    fun handleScan(stationId: String) {
        when (_scanIntent.value) {
            ScanIntent.RETURN -> endCurrentRental(stationId)
            ScanIntent.START  -> createReservation(stationId)
        }
    }
    // ——— NUEVO: flujo del alquiler actual desde local (puede ser null) ———
    val activeRental: StateFlow<Rental?> =
        rentalUseCases.getCurrentRentalUseCase() // Flow<Rental?>
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // ——— NUEVO: booleano derivado para que la UI solo consuma si hay activo ———
    val hasActive: StateFlow<Boolean> =
        activeRental
            .map { it?.let(::isRentalActive) == true }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    // Regla de “activo”: ajusta estados según tu backend
    private fun isRentalActive(r: Rental): Boolean {
        val status = r.status.orEmpty().lowercase(Locale.ROOT)
        val statusActivos = setOf("ongoing", "active", "started", "reserved")
        return r.endedAt == null && (status.isBlank() || status in statusActivos)
    }

    fun reset() {
        _rentState.value = RentState.Idle
    }

    fun createReservation(stationId: String) {
        viewModelScope.launch {
            _rentState.value = RentState.Loading
            try {
                val user = userUseCases.getUserUseCase().first()
                if (user == null) {
                    _rentState.value = RentState.Error("Usuario no autenticado")
                    return@launch
                }

                val rental = Rental(
                    userId = user.id,
                    stationStartId = stationId,
                    authType = "nfc",
                )

                val created = rentalUseCases.createRentalUseCase.invoke(rental)
                _rentState.value = RentState.Success(created)

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
                // Crea una copia con los datos necesarios para terminarla
                val rentalToEnd = current.copy(
                    userId = user.id,
                    stationStartId = stationId, // o usa un campo diferente si tu backend lo requiere
                    endedAt = java.time.Instant.now().toString(),
                    status = "completed" // o el estado que tu API espera
                )

                // Envía la solicitud al backend
                val ended = rentalUseCases.endRentalUseCase.invoke(rentalToEnd)

                // Éxito → actualiza estado
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
