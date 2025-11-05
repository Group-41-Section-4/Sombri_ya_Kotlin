package com.example.sombriyakotlin.domain.usecase.stations

import com.example.sombriyakotlin.domain.model.Localization
import com.example.sombriyakotlin.domain.model.Station
import com.example.sombriyakotlin.domain.repository.StationRepository
import javax.inject.Inject

class GetStationsUseCase @Inject constructor(
    private val stationRepository: StationRepository
) {
    suspend operator fun invoke(localization: Localization): List<Station> {
        return stationRepository.getStations(localization)
    }
}