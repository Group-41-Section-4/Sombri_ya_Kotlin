package com.example.sombriyakotlin.ui.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.sombriyakotlin.data.datasource.stations.SimpleStationCache
import com.example.sombriyakotlin.data.datasource.stations.StationsCacheLocalDataSource
import com.example.sombriyakotlin.data.dto.toDomain
import com.example.sombriyakotlin.data.repository.StationsCacheRepository
import com.example.sombriyakotlin.dataStore
import com.example.sombriyakotlin.domain.model.Localization
import com.example.sombriyakotlin.domain.model.Station
import com.example.sombriyakotlin.domain.usecase.ObserveConnectivityUseCase
import com.example.sombriyakotlin.domain.usecase.stations.StationsUseCases

import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StationsViewModel @Inject constructor(
    private val stationsUseCase: StationsUseCases,
    private val observeConnectivity: ObserveConnectivityUseCase,
    @ApplicationContext private val context: Context
) : ViewModel(){
    sealed class StationsState {
        object Idle : StationsState()
        object Loading : StationsState()
        data class Success(val stations: List<Station>) : StationsState()
        data class Error(val message: String) : StationsState()
    }

    val isConnected: StateFlow<Boolean> = observeConnectivity()

    private val _stationsState = MutableStateFlow<StationsState>(StationsState.Idle)
    val stationsState: StateFlow<StationsState> = _stationsState


    fun getStations(latitud: Double, longitud: Double) {
        Log.d("StationsViewModel", "Obteniendo estaciones para latitud: $latitud, longitud: $longitud")
        viewModelScope.launch {
            _stationsState.value = StationsState.Loading
            try {
                val localization = Localization(latitud, longitud)

                val cacheRepo = StationsCacheRepository(
                    StationsCacheLocalDataSource(context.dataStore)
                )
                Log.d("HOLLY","FUNCIONAA $(isConnected.value)")
                var stations : List<Station>
                if (isConnected.value) {
                    stations = stationsUseCase.getStationsUseCase.invoke(localization)
                } else {
                    val cacheStations = cacheRepo.getCachedStationsFreshOrEmpty()
                    Log.d("StationsViewModel","cacheStations: $cacheStations")
                    stations = cacheStations.map{st -> st.toDomain()}
                }

                // Guardar estaciones
                val simple = stations.map { st ->
                    SimpleStationCache(
                        id = st.id ?: "",
                        name = st.placeName ?: "",
                        latitude = st.latitude ?: 0.0,
                        longitude = st.longitude ?: 0.0
                    )
                }



                try {
                    cacheRepo.saveAll(simple)
                    Log.d("HOLLY","✅ Guardado en cache OK (${simple.size})")
                } catch (ex: Exception) {
                    Log.e("HOLLY","❌ Falló saveAll", ex)
                }

                _stationsState.value = StationsState.Success(stations)
                Log.d("StationsViewModel", "Estaciones obtenidas: $stations")
            } catch (e: Exception) {
                // 3. AÑADIMOS UN LOG PARA VER EL ERROR EN LOGCAT
                Log.e("StationsViewModel", "Error al obtener estaciones: ${e.message}", e)
                _stationsState.value = StationsState.Error("Error al obtener estaciones: ${e.message}")
            }
        }

    }
}