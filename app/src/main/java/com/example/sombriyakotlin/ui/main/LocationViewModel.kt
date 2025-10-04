package com.example.sombriyakotlin.ui.main
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.data.location.GPSManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import android.location.Location

class LocationViewModel : ViewModel() {

    val locationState = GPSManager.observeLocationFlow()
        .map<Location, Location?> { it }      // identity, pero puedes mapear a DTO propio
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}