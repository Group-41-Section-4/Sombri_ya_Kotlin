package com.example.sombriyakotlin.data.dto

import com.example.sombriyakotlin.domain.model.Rental

data class RentalDto(
    val id: Int,              // UUID
    val userId: String,          // UUID del usuario
    val stationStartId: String,  // UUID de la estación inicial
    val paymentMethodId: String?,// UUID opcional
    val startLat: Double,
    val startLon: Double,
    val authType: String,        // o mejor enum si lo mapeas
    val status: String,          // ongoing, completed, etc.
    val startedAt: String,       // ISO-8601
    val endedAt: String?         // puede ser null
)

fun RentalDto.toDomain(): Rental = Rental(id = id, userId = userId, stationStartId = stationStartId, paymentMethodId = paymentMethodId, startLat = startLat, startLon = startLon, authType = authType, status = status, startedAt = startedAt, endedAt = endedAt)

fun Rental.toDto(): RentalDto = RentalDto(
    id = id,
    userId = userId,
    stationStartId = stationStartId,
    paymentMethodId = paymentMethodId,
    startLat = startLat,
    startLon = startLon,
    authType = authType,
    status = status,
    startedAt = startedAt,
    endedAt = endedAt
)
