package com.example.sombriyakotlin.domain.repository

interface WeatherRepository {
    suspend fun getFirstPopPercent(lat: Double, lon: Double): Int?
}