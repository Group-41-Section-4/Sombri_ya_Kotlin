package com.example.sombriyakotlin.ui.layout

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.ui.PedometerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PedometerViewModel @Inject constructor(
    private val manager: PedometerManager
) : ViewModel() {

    val count: StateFlow<Int> = manager.totalSteps

    val isRunning: StateFlow<Boolean> = manager.isRunning

    fun startPedometer() {
        viewModelScope.launch {
            try {
                manager.startIfNeeded()
            } catch (e: Exception) {
                Log.e("PedometerVM", "Failed to start pedometer", e)
            }
        }
    }

    fun stopPedometer() {
        viewModelScope.launch {
            manager.stopIfNeeded()
        }
    }

//    fun resetLocalCount() {
//        // Si quieres reset persistente, implementa en manager/repo.
//    }
}
