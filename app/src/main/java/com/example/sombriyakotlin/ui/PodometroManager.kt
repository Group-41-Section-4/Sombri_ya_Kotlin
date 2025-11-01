package com.example.sombriyakotlin.ui

import com.example.sombriyakotlin.ApplicationScope
import com.example.sombriyakotlin.domain.model.PedometerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PedometerManager @Inject constructor(
    private val repo: PedometerRepository,
    @ApplicationScope private val appScope: CoroutineScope // provide appScope via Hilt
) {
    private val refsLock = Any()
    private var refs = 0

    // Estado público
    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    // Total acumulado de pasos (Int para UI; si necesitas, cambia a Long)
    private val _totalSteps = MutableStateFlow(0)
    val totalSteps: StateFlow<Int> = _totalSteps.asStateFlow()

    private var collectionJob: Job? = null

    suspend fun startIfNeeded() {
        var shouldStart = false
        synchronized(refsLock) {
            refs++
            if (refs == 1) shouldStart = true
        }
        if (shouldStart) {
            try {
                repo.start()
                startCollecting()
                _isRunning.value = true
            } catch (e: Exception) {
                // manejar fallo de start (sin sensor, permisos, etc.)
                synchronized(refsLock) { refs = (refs - 1).coerceAtLeast(0) }
                throw e
            }
        }
    }

    suspend fun stopIfNeeded() {
        var shouldStop = false
        synchronized(refsLock) {
            refs = (refs - 1).coerceAtLeast(0)
            if (refs == 0) shouldStop = true
        }
        if (shouldStop) {
            stopCollecting()
            repo.stop()
            _isRunning.value = false
        }
    }

    private fun startCollecting() {
        // lanzar en appScope para que sobreviva a ViewModels
        if (collectionJob?.isActive == true) return
        collectionJob = appScope.launch {
            repo.observeStepEvents().collect { ev ->
                // acumular pasos; si StepEvent.steps es Long, adapta aquí
                val next = (_totalSteps.value + ev.steps).coerceAtMost(Int.MAX_VALUE)
                _totalSteps.value = next
            }
        }
    }

    private fun stopCollecting() {
        collectionJob?.cancel()
        collectionJob = null
    }
}
