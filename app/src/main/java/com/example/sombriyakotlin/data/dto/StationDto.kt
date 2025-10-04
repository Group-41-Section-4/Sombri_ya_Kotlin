package com.example.sombriyakotlin.data.dto

import androidx.compose.ui.geometry.CornerRadius
import com.example.sombriyakotlin.domain.model.Localization
import com.example.sombriyakotlin.domain.model.Station
import com.example.sombriyakotlin.domain.model.User

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



fun StationDto.toDomain(): Station = Station(id, placeName, description, latitude, longitude, distanceMeters, availableUmbrellas, totalUmbrellas)
