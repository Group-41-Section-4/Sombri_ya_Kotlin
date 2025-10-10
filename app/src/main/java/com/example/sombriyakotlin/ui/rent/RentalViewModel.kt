package com.example.sombriyakotlin.ui.rent

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.domain.model.Rental
import com.example.sombriyakotlin.domain.usecase.rental.RentalUseCases
import com.example.sombriyakotlin.domain.usecase.stations.StationsUseCases
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
    private val stationUseCases: StationsUseCases
) : ViewModel() {

    sealed class RentState {
        object Idle : RentState()
        object Loading : RentState()
        data class Success(val rental: Rental) : RentState()
        data class Error(val message: String) : RentState()
    }
    enum class ScanIntent { START, RETURN, STARTQR }


    private val _rentState = MutableStateFlow<RentState>(RentState.Idle)
    val rentState: StateFlow<RentState> = _rentState

    private val _scanIntent = MutableStateFlow(ScanIntent.START)
    val scanIntent: StateFlow<ScanIntent> = _scanIntent

    fun setReturnIntent() { _scanIntent.value = ScanIntent.RETURN }
    fun setStartIntent()  { _scanIntent.value = ScanIntent.START }
    fun setStartQrIntent()  { _scanIntent.value = ScanIntent.STARTQR }


    fun handleScanNfc(tagUid: String) {
        viewModelScope.launch {
            val stationId = stationUseCases.getStationByTagUseCase.invoke(tagUid)
            handleScan(stationId)
        }

    }

    fun handleScan(stationId: String) {
        when (_scanIntent.value) {
            ScanIntent.RETURN -> endCurrentRental(stationId)
            ScanIntent.START  -> createReservation(stationId)
            ScanIntent.STARTQR -> createReservationIdStation(stationId)
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

    fun loading() {
        _rentState.value = RentState.Loading
    }

    fun success(created: Rental) {
        _rentState.value = RentState.Success(created)
    }

    fun error(message: String) {
        _rentState.value = RentState.Error(message)
    }

    fun createReservation(tagUid: String) {
        viewModelScope.launch {
            _rentState.value = RentState.Loading
            try {
                val user = userUseCases.getUserUseCase().first()
                if (user == null) {
                    _rentState.value = RentState.Error("Usuario no autenticado")
                    return@launch
                }
                Log.d("RENT", "ya a punto ${tagUid}")
                // val stationId = stationUseCases.getStationByTagUseCase.invoke(tagUid)
                val stationId = tagUid
                Log.d("RENT", "passsssaa")

                if (stationId == null) {
                    _rentState.value = RentState.Error("No se encontró la estación con el UID proporcionado")
                    return@launch
                }
                val rental = Rental(
                    userId = user.id,
                    stationStartId = stationId,
                    authType = "nfc",
                )
                Log.d("RENT", "ya a punto")
                val created = rentalUseCases.createRentalUseCase.invoke(rental)
                Log.d("RENT", "creada")
                _rentState.value = RentState.Success(created)
                Log.d("RENT", "guardada")

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

    fun createReservationIdStation(stationId: String) {
        Log.d("RENT", "Se empieza a crear la renta")
        viewModelScope.launch {
            loading()

            try {

                val user = userUseCases.getUserUseCase().first()
                if (user == null) {
                    error("Usuario no autenticado")
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
                success(created)
                Log.d("PASO", "guardada")


            } catch (e: Exception) {
                val errorMessage = if (e is HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    "Error ${e.code()}: ${errorBody ?: "Solicitud inválida"}"
                } else {
                    e.message ?: "Error desconocido"
                }
                error(errorMessage)
            }
        }
    }

    fun endCurrentRental(stationId: String) {
        viewModelScope.launch {
            Log.d("RENT", "Se termina la renta")

            _rentState.value = RentState.Loading

            try {
                val current = rentalUseCases.getCurrentRentalUseCase().first()
                if (current == null) {
                    _rentState.value = RentState.Error("No hay un alquiler activo")
                    Log.d("RENT", "No hay un alquiler activo")

                    return@launch
                }
                val user = userUseCases.getUserUseCase().first()
                if (user == null) {
                    _rentState.value = RentState.Error("Usuario no autenticado")
                    Log.d("RENT", "Usuario no autenticado")
                    return@launch
                }

                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val endDate = sdf.format(Date())

                // Crea una copia con los datos necesarios para terminarla
                val rentalToEnd = current.copy(
                    userId = user.id,
                    stationStartId = stationId,
                    endedAt = endDate,
                    status = "completed"
                )
                Log.d("RENT", "Se termina la renta")
                val ended = rentalUseCases.endRentalUseCase.invoke(rentalToEnd)
                Log.d("RENT", "Se termina la renta")

                _rentState.value = RentState.Success(ended)

            } catch (e: Exception) {
                val msg = if (e is retrofit2.HttpException) {
                    val body = e.response()?.errorBody()?.string()
                    "Error ${e.code()}: ${body ?: "Solicitud inválida"}"
                } else {
                    e.message ?: "Error desconocido"
                }
                _rentState.value = RentState.Error(msg)
                Log.d("RENT", "Error al terminar la renta")
            }
        }
    }






}
