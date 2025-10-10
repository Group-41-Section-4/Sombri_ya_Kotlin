package com.example.sombriyakotlin.domain.repository

import com.example.sombriyakotlin.domain.model.Localization
import com.example.sombriyakotlin.domain.model.Station

interface StationRepository {
    // Renamed for clarity
    suspend fun getStations(location: Localization): List<Station>    // Example of another function you might add in the future
    suspend fun getStationByTag(uid: String): String
}
