package com.example.sombriyakotlin.data.dto

import com.example.sombriyakotlin.domain.model.Rental

data class RentalDto(
    val userId: String,
    val stationStartId: String,
    val paymentMethodId: String?,
    val start_gps: StartGps,
    val authType: String,
    val status: String,
    val startedAt: String,
    val endedAt: String?
)

data class RentalRequest(
    val user_id: String,
    val station_start_id: String,
    val start_gps: StartGps,
    val auth_type: String
)

data class StartGps(
    val latitude: Double,
    val longitude: Double
)


fun RentalDto.toDomain(): Rental = Rental(
    id = 0, // si el backend no lo devuelve, ponle un valor por defecto
    userId = userId,
    stationStartId = stationStartId,
    paymentMethodId = paymentMethodId,
    startLat = start_gps.latitude,
    startLon = start_gps.longitude,
    authType = authType,
    status = status,
    startedAt = startedAt,
    endedAt = endedAt
)


fun Rental.toDto(): RentalDto {
    return RentalDto(
        userId = userId,
        stationStartId = stationStartId,
        paymentMethodId = paymentMethodId,
        start_gps = StartGps(startLat, startLon),
        authType = authType,
        status = status,
        startedAt = startedAt,
        endedAt = endedAt
    )
}

fun Rental.toRequest(): RentalRequest {
    return RentalRequest(
        user_id = userId,
        station_start_id = stationStartId,
        start_gps = StartGps(startLat, startLon),
        auth_type = authType
    )
}
