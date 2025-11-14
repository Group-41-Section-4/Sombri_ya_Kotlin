package com.example.sombriyakotlin.ui.splash

import androidx.lifecycle.ViewModel
import com.example.sombriyakotlin.domain.usecase.ObserveConnectivityUseCase
import com.example.sombriyakotlin.domain.usecase.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    observeConnectivity: ObserveConnectivityUseCase,
    private val getUserUseCase: GetUserUseCase
    ) : ViewModel() {

    val isConnected: StateFlow<Boolean> = observeConnectivity()

    suspend fun hasUserStored(): Boolean {
        return getUserUseCase().firstOrNull() != null
    }
}