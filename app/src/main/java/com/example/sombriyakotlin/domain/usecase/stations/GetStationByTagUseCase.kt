package com.example.sombriyakotlin.domain.usecase.stations

import com.example.sombriyakotlin.domain.model.Localization
import com.example.sombriyakotlin.domain.model.Station
import com.example.sombriyakotlin.domain.repository.StationRepository
import javax.inject.Inject

class GetStationByTagUseCase @Inject constructor (
    private val stationRepository: StationRepository

){
    suspend fun invoke(uid: String): String {
        return stationRepository.getStationByTag(uid)
    }

}