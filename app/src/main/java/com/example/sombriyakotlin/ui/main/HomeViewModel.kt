package com.example.sombriyakotlin.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.domain.model.Notification
import com.example.sombriyakotlin.domain.model.WeatherType
import com.example.sombriyakotlin.domain.repository.WeatherRepository
import com.example.sombriyakotlin.domain.usecase.ObserveConnectivityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val observeConnectivity: ObserveConnectivityUseCase,
): ViewModel(){

    val isConnected: StateFlow<Boolean> = observeConnectivity()

    private val _weatherState = MutableStateFlow<WeatherType?>(null)
    val weatherState: StateFlow<WeatherType?> = _weatherState

    var lastWeatherUpdateTime: Long = 0L
        private set

    fun checkWeatherAt(lat: Double, lon: Double) {
        viewModelScope.launch {
            if (!isConnected.value) return@launch
            val pop = weatherRepository.getFirstPopPercent(lat, lon) ?: return@launch
            val weather = when {
                pop > 70 -> WeatherType.LLUVIA
                pop > 30 -> WeatherType.NUBLADO
                else -> WeatherType.SOLEADO
            }
            _weatherState.value = weather
            lastWeatherUpdateTime = System.currentTimeMillis()
            Log.d("HomeViewModel", "checkWeatherAt: $weather")
        }
    }

}