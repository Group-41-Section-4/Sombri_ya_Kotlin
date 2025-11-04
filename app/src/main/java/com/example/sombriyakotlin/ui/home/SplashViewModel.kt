package com.example.sombriyakotlin.ui.home

import androidx.lifecycle.ViewModel
import com.example.sombriyakotlin.domain.usecase.ObserveConnectivityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    observeConnectivity: ObserveConnectivityUseCase
) : ViewModel() {

    val isConnected: StateFlow<Boolean> = observeConnectivity()
}