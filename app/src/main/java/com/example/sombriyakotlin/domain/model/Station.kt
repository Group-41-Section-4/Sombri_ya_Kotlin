package com.example.sombriyakotlin.domain.model

data class Station(
    val id: String,
    val placeName: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val distanceMeters: Int,
    val availableUmbrellas: Int,
    val totalUmbrellas: Int
)

