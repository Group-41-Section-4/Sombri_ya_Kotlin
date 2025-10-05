package com.example.sombriyakotlin.ui.rent

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.domain.model.Rental
import com.example.sombriyakotlin.domain.usecase.rental.RentalUseCases
import com.example.sombriyakotlin.domain.usecase.user.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException
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

    private val _rentState = MutableStateFlow<RentState>(RentState.Idle)
    val rentState: StateFlow<RentState> = _rentState

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
}