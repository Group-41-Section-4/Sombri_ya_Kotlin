package com.example.sombriyakotlin.domain.model

import kotlinx.coroutines.flow.Flow

data class StepEvent(val timestamp: Long, val steps: Int)

interface PedometerRepository {
    fun observeStepEvents(): Flow<StepEvent>
    suspend fun start()
    suspend fun stop()
    suspend fun reset()
    fun isRunning(): Boolean
}

class ObserveSteps(private val repo: PedometerRepository) {
    operator fun invoke(): Flow<StepEvent> = repo.observeStepEvents()
}

class StartPedometer(private val repo: PedometerRepository) {
    suspend operator fun invoke() = repo.start()
}

class StopPedometer(private val repo: PedometerRepository) {
    suspend operator fun invoke() = repo.stop()
}

class ResetPedometer(private val repo: PedometerRepository) {
    suspend operator fun invoke() = repo.reset()
}