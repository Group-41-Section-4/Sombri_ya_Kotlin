package com.example.sombriyakotlin.domain.repository

import com.example.sombriyakotlin.domain.model.Localization
import com.example.sombriyakotlin.domain.model.Station

interface StationRepository {
    suspend fun getStations(localization: Localization): List<Station>

}