package com.example.sombriyakotlin.data.repository

import android.util.Log
import com.example.sombriyakotlin.data.api.StationApi
import com.example.sombriyakotlin.data.dto.toDomain
import com.example.sombriyakotlin.data.dto.toDto
import com.example.sombriyakotlin.domain.model.Localization
import com.example.sombriyakotlin.domain.model.Station
import com.example.sombriyakotlin.domain.repository.StationRepository
import javax.inject.Inject

class StationRepositoryImpl @Inject constructor(
   private val stationApi: StationApi
) : StationRepository {

    override suspend fun getStations(location: Localization): List<Station> {
        val response = stationApi.getStations(location.toDto())
        Log.d("StationRepositoryImpl", "Response from API: ${response.body()}")

        if (response.isSuccessful) {
            val stationDtoList = response.body() ?: emptyList()
            return stationDtoList.map { it.toDomain() }
        } else {
            // Here you can handle the error, for example by throwing an exception or returning an empty list
            Log.e("StationRepositoryImpl", "Error getting stations: ${response.code()}")
            return emptyList()
        }
    }
    override suspend fun getStationByTag(uid: String): String {
        Log.d("StationRepositoryImpl", "Q ,PASOOO")

        val response = stationApi.getStationByTag(uid)
        Log.d("StationRepositoryImpl", "PAPI MIRALO VIEB ${response.id}")
        return response.id
    }

}