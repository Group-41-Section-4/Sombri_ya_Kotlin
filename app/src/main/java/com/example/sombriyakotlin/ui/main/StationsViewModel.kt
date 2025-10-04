package com.example.sombriyakotlin.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.domain.model.Localization
import com.example.sombriyakotlin.domain.model.Station
import com.example.sombriyakotlin.domain.usecase.stations.GetStationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StationsViewModel @Inject constructor(
    private val getStationsUseCase: GetStationsUseCase
) : ViewModel(){
    sealed class StationsState {
        object Idle : StationsState()
        object Loading : StationsState()
        data class Success(val stations: List<Station>) : StationsState()
        data class Error(val message: String) : StationsState()
    }

    private val _stationsState = MutableStateFlow<StationsState>(StationsState.Idle)
    val stationsState: StateFlow<StationsState> = _stationsState


    fun getStations(latitud: Double, longitud: Double) {
        viewModelScope.launch {
            _stationsState.value = StationsState.Loading
            try {
                val localization = Localization(latitud, longitud)

                val stations = getStationsUseCase.invoke(localization)
                _stationsState.value = StationsState.Success(stations)
            } catch (e: Exception) {
                // 3. AÑADIMOS UN LOG PARA VER EL ERROR EN LOGCAT
                Log.e("StationsViewModel", "Error al obtener estaciones: ${e.message}", e)
                _stationsState.value = StationsState.Error("Error al obtener estaciones: ${e.message}")
            }
        }

    }
}