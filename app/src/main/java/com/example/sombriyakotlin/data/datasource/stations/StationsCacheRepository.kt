package com.example.sombriyakotlin.data.repository

import com.example.sombriyakotlin.data.datasource.stations.SimpleStationCache
import com.example.sombriyakotlin.data.datasource.stations.StationsCacheLocalDataSource

class StationsCacheRepository(
    private val local: StationsCacheLocalDataSource
) {
    private val DEFAULT_TTL = Long.MAX_VALUE

    suspend fun getCachedStationsFreshOrEmpty(ttlMillis: Long = DEFAULT_TTL): List<SimpleStationCache> {
        val disk = local.getStationsOrEmpty()
        return if (disk.isNotEmpty() && local.isFresh(ttlMillis)) disk else emptyList()
    }

    suspend fun saveAll(stations: List<SimpleStationCache>) {
        local.saveStations(stations)
    }

    suspend fun clear() {
        local.clear()
    }
}
