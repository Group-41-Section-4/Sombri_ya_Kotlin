package com.example.sombriyakotlin.ui.layout


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.domain.model.ObserveSteps
import com.example.sombriyakotlin.domain.model.PedometerRepository
import com.example.sombriyakotlin.domain.model.ResetPedometer
import com.example.sombriyakotlin.domain.model.StartPedometer
import com.example.sombriyakotlin.domain.model.StopPedometer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class PedometerViewModel @Inject constructor(
    private val repo: PedometerRepository
) : ViewModel() {

    private val observeSteps = ObserveSteps(repo)
    private val start = StartPedometer(repo)
    private val stop = StopPedometer(repo)
    private val reset = ResetPedometer(repo)

    private val _count = MutableStateFlow(0)
    val count: StateFlow<Int> = _count

    private var collectingJob: kotlinx.coroutines.Job? = null

    fun startPedometer() {
        viewModelScope.launch {
            start()
            // Cancelamos cualquier recolección anterior
            collectingJob?.cancel()

            // Asignamos el nuevo Job del flujo de pasos
            collectingJob = launch {
                observeSteps().collectLatest { event ->
                    _count.value += event.steps
                }
            }
        }
    }


    fun stopPedometer() {
        viewModelScope.launch {
            stop()
            collectingJob?.cancel()
        }
    }

    fun resetPedometer() {
        viewModelScope.launch {
            reset()
            _count.value = 0
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch { stop() }
    }
}