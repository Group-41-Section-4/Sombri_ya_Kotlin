package com.example.sombriyakotlin.data.dto

import com.example.sombriyakotlin.data.datasource.stations.SimpleStationCache
import com.example.sombriyakotlin.domain.model.Station

data class StationDto(
    val id: String,
    val placeName: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val distanceMeters: Int,
    val availableUmbrellas: Int,
    val totalUmbrellas: Int
)


data class QrStationDto(
    val station_id: String,
)



fun StationDto.toDomain(): Station = Station(id, placeName, description, latitude, longitude, distanceMeters, availableUmbrellas, totalUmbrellas)


fun SimpleStationCache.toDomain(): Station = Station(id, name, "Estacion en cache", latitude, longitude, -1, -1, -1)