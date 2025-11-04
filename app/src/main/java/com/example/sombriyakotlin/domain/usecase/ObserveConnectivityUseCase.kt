package com.example.sombriyakotlin.domain.usecase

import com.example.sombriyakotlin.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ObserveConnectivityUseCase @Inject constructor(
    private val repository: NetworkRepository
) {
    operator fun invoke(): StateFlow<Boolean> = repository.isConnected
}