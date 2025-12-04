package com.example.sombriyakotlin.domain.model

data class Rental(
    val id: Int = 0,
    val userId: String = "",
    val stationStartId: String = "",
    val paymentMethodId: String? = null,
    val startLat: Double = 0.0,
    val startLon: Double = 0.0,
    val authType: String = "",
    val status: String = "",
    val startedAt: String = "",
    val endedAt: String? = null,
    val steps: Int = 0
)