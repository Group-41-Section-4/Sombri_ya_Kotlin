package com.example.sombriyakotlin.data.dto

import com.example.sombriyakotlin.domain.model.Localization

data class QueryLocalizationDto(
    val latitude: Double,
    val longitude: Double,
    val radius_m: Int,

    )
fun Localization.toDto(): QueryLocalizationDto = QueryLocalizationDto(latitude, longitude, 3600000)