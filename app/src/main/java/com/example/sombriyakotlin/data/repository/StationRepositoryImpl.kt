package com.example.sombriyakotlin.data.repository

import android.util.Log
import com.example.sombriyakotlin.data.api.StationApi
import com.example.sombriyakotlin.data.cache.LruCache
import com.example.sombriyakotlin.data.dto.toDomain
import com.example.sombriyakotlin.data.dto.toDto
import com.example.sombriyakotlin.domain.model.Localization
import com.example.sombriyakotlin.domain.model.Station
import com.example.sombriyakotlin.domain.repository.StationRepository
import javax.inject.Inject

class StationRepositoryImpl @Inject constructor(
    private val cache: LruCache<String, List<Station>>,
    private val stationApi: StationApi
) : StationRepository {

    override suspend fun getStations(location: Localization): List<Station> {

        val formattedLatitude = String.format("%.3f", location.latitude)
        val formattedLongitude = String.format("%.3f", location.longitude)
        val key = "$formattedLatitude:$formattedLongitude"
        cache.get(key)?.let {
            Log.d("StationRepositoryImpl", "Cache hit para $key (${it.size} estaciones)")
            return it
        }

        Log.d("StationRepositoryImpl", "Cache miss. Llamando a API...")
        val response = stationApi.getStations(location.toDto())
        Log.d("StationRepositoryImpl", "Response from API: ${response.body()}")

        if (response.isSuccessful) {
            val stationDtoList = response.body() ?: emptyList()
            val stations = stationDtoList.map { it.toDomain() }

            cache.put(key, stations)
            return stations
        } else {
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