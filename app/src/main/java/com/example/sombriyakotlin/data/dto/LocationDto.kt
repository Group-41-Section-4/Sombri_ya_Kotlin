package com.example.sombriyakotlin.data.dto

import com.example.sombriyakotlin.domain.model.CreateLocation
import com.example.sombriyakotlin.domain.model.Localization

data class QueryLocalizationDto(
    val latitude: Double,
    val longitude: Double,
    val radius_m: Int,

    )

data class CreateLocationDto(
    val latitude: Double,
    val longitude: Double,
    val userId: String
)


fun CreateLocation.toDto(): CreateLocationDto= CreateLocationDto(
    latitude = latitude,
    longitude = longitude,
    userId = user_id
)

fun Localization.toDto(): QueryLocalizationDto = QueryLocalizationDto(latitude, longitude, 3600000)