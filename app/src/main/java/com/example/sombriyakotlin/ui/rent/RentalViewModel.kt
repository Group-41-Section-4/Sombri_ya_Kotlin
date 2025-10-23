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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class RentViewModel @Inject constructor(
    private val rentalUseCases: RentalUseCases,
    private val userUseCases: UserUseCases,
) : ViewModel() {

    // ---- UI State ----
    sealed class RentState {
        object Idle : RentState()
        object Loading : RentState()
        data class Success(val rental: Rental) : RentState()
        data class Error(val message: String) : RentState()
    }

    enum class ScanIntent { START, RETURN, STARTQR,RETURNQR } // START/RETURN para NFC, STARTQR para QR
    private val _rentState = MutableStateFlow<RentState>(RentState.Idle)
    val rentState: StateFlow<RentState> = _rentState

    private val _scanIntent = MutableStateFlow(ScanIntent.START)
    val scanIntent: StateFlow<ScanIntent> = _scanIntent

    fun setReturnIntent()   { _scanIntent.value = ScanIntent.RETURN }
    fun setQr(){
        if (_scanIntent.value == ScanIntent.START){_scanIntent.value = ScanIntent.STARTQR}
        else if(_scanIntent.value == ScanIntent.RETURN) {_scanIntent.value = ScanIntent.RETURNQR}

    }
    // ---- Alquiler activo desde local (puede ser null) ----
    val activeRental: StateFlow<Rental?> =
        rentalUseCases.getCurrentRentalUseCase() // Flow<Rental?>
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // ---- Booleano derivado para la UI ----
    val hasActive: StateFlow<Boolean> =
        activeRental
            .map { it?.let(::isRentalActive) == true }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    /**
     * Determina si una renta sigue activa
     */
    private fun isRentalActive(r: Rental): Boolean {
        val status = r.status.orEmpty().lowercase(Locale.ROOT)
        val statusActivos = setOf("ongoing", "active", "started", "reserved")
        return r.endedAt == null && (status.isBlank() || status in statusActivos)
    }

    /**
     * Permite consultar directamente si hay una renta activa.
     */
    suspend fun checkActiveRental(): Boolean {
        val current = rentalUseCases.getCurrentRentalUseCase().first()
        val active = current?.let(::isRentalActive) == true
        Log.d("RENT_CHECK", "¿Hay renta activa?: $active (renta=$current)")
        return active
    }

    fun reset()            { _rentState.value = RentState.Idle }
    fun loading()          { _rentState.value = RentState.Loading }
    fun success(created: Rental) { _rentState.value = RentState.Success(created) }
    fun error(message: String)   { _rentState.value = RentState.Error(message) }

    // ---------------- CREAR RENTA ----------------

    fun createReservation(tagUid: String) {
        viewModelScope.launch {
            _rentState.value = RentState.Loading
            try {
                val user = userUseCases.getUserUseCase().first() ?: run {
                    _rentState.value = RentState.Error("Usuario no autenticado")
                    return@launch
                }

                val stationId = "16fdfe43-72a3-496e-8d80-7cb5071cff8e"

                val rental = Rental(
                    userId = user.id,
                    stationStartId = stationId,
                    authType = "nfc",
                )

                val created = rentalUseCases.createRentalUseCase.invoke(rental)
                _rentState.value = RentState.Success(created)
                Log.d("RENTANDO", "Renta creada: ${_rentState.value}")
            } catch (e: Exception) {
                val msg = if (e is HttpException) {
                    val body = e.response()?.errorBody()?.string()
                    "Error ${e.code()}: ${body ?: "Solicitud inválida"}"
                } else e.message ?: "Error desconocido"
                _rentState.value = RentState.Error(msg)
            }
        }
    }

    // ---------------- QR ----------------

    fun handleScan(input: String) {
        viewModelScope.launch {
            try {
                when (_scanIntent.value) {
                    ScanIntent.START -> createReservation(input)
                    ScanIntent.RETURN -> {
                        Log.d("RENT", "Iniciando devolución por NFC")
                        val stationId = "16fdfe43-72a3-496e-8d80-7cb5071cff8e"
                        endCurrentRental(stationId)
                    }
                    ScanIntent.STARTQR -> {
                        Log.d("WTFFFF", "QRRRRRRRRRRRR")
                        createReservationIdStation(input)
                    }

                    ScanIntent.RETURNQR -> {
                        Log.d("WTFFFF", "Se empieza a devolver la sombrilla")

                        endCurrentRental(input)
                        Log.d("WTFFFF", "Se devolvio")

                    }
                }
            } catch (e: Exception) {
                error(e.message ?: "Error procesando escaneo")
            }
        }
    }

    fun createReservationIdStation(stationId: String) {
        Log.d("RENT", "Creando renta por QR")
        viewModelScope.launch {
            loading()
            try {
                val user = userUseCases.getUserUseCase().first() ?: run {
                    error("Usuario no autenticado")
                    return@launch
                }

                val rental = Rental(
                    userId = user.id,
                    stationStartId = stationId,
                    authType = "qr",
                )

                val created = rentalUseCases.createRentalUseCase.invoke(rental)
                success(created)
            } catch (e: Exception) {
                val msg = if (e is HttpException) {
                    val body = e.response()?.errorBody()?.string()
                    "Error ${e.code()}: ${body ?: "Solicitud inválida"}"
                } else e.message ?: "Error desconocido"
                error(msg)
            }
        }
    }

    // ---------------- TERMINAR RENTA ----------------

    fun endCurrentRental(stationId: String) {
        viewModelScope.launch {
            Log.d("RENT", "Terminando renta...")
            _rentState.value = RentState.Loading
            try {
                val current = rentalUseCases.getCurrentRentalUseCase().first()
                if (current == null) {
                    _rentState.value = RentState.Error("No hay alquiler activo")
                    return@launch
                }

                val user = userUseCases.getUserUseCase().first() ?: run {
                    _rentState.value = RentState.Error("Usuario no autenticado")
                    return@launch
                }

                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val endDate = sdf.format(Date())

                val rentalToEnd = current.copy(
                    userId = user.id,
                    stationStartId = stationId,
                    endedAt = endDate,
                    status = "completed",
                    authType = current.authType ?: "nfc"
                )

                val ended = rentalUseCases.endRentalUseCase.invoke(rentalToEnd)
                val finalRental = if (ended.endedAt.isNullOrBlank()) {
                    ended.copy(endedAt = rentalToEnd.endedAt)
                } else ended

                Log.d("RENT", "Renta finalizada correctamente")
                _rentState.value = RentState.Success(finalRental)
            } catch (e: Exception) {
                val msg = if (e is HttpException) {
                    val body = e.response()?.errorBody()?.string()
                    "Error ${e.code()}: ${body ?: "Solicitud inválida"}"
                } else e.message ?: "Error desconocido"
                _rentState.value = RentState.Error(msg)
            }
        }
    }
}
