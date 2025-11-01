package com.example.sombriyakotlin.data.datasource.stations

import kotlinx.serialization.Serializable

@Serializable
data class SimpleStationCache(
    val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double
)
