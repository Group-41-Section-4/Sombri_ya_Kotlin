package com.example.sombriyakotlin.data.dto

import android.util.Log
import com.example.sombriyakotlin.domain.model.Rental

/* ------------------ DTOs (nullables para reflejar el backend) ------------------ */

data class RentalDto(
    val userId: String? = null,
    val stationStartId: String? = null,
    val paymentMethodId: String? = null,
    val start_gps: StartGps? = null,
    val authType: String? = null,
    val status: String? = null,
    val startedAt: String? = null,
    val endedAt: String? = null,
    // Si tu backend devuelve un UUID (rental_id), puedes añadirlo:
    val rental_id: String? = null
)

data class RentalRequestDto(
    val user_id: String,
    val station_start_id: String,
    val auth_type: String
)

data class EndRentalDto(
    val user_id: String,
    val station_end_id: String,
)

data class StartGps(
    val latitude: Double? = null,
    val longitude: Double? = null
)

/* ------------------ Mappers ------------------ */

// DTO -> Dominio
fun RentalDto.toDomain(): Rental = Rental(
    id = 0,                                        // El DTO no trae id numérico: usa 0
    userId = userId ?: "",                         // evita NPE si backend no lo manda
    stationStartId = stationStartId ?: "",         // si en dominio es no-nulo, usa vacío por defecto
    paymentMethodId = paymentMethodId,             // puede ser null en dominio
    startLat = start_gps?.latitude ?: 0.0,         // evita NPE cuando no mandan start_gps
    startLon = start_gps?.longitude ?: 0.0,
    authType = authType ?: "",                     // si dominio lo exige no-nulo
    status = status ?: "",                         // idem
    startedAt = startedAt ?: "",                   // si en dominio es no-nulo
    endedAt = endedAt                              // si en dominio permite null, déjalo así
)

// Dominio -> DTO (por si necesitas enviarlo al backend, aunque normalmente se usa RequestDto)
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

// Dominio -> DTO de inicio de renta (para POST /rentals/start)
fun Rental.toRequestDto(): RentalRequestDto {
    val request = RentalRequestDto(
        user_id = userId,
        station_start_id = stationStartId,
        auth_type = authType
    )
    Log.d("RENTAL_REQUEST", "DTO enviado: $request")
    return request
}

// Dominio -> DTO de fin de renta
fun Rental.toEndDto(): EndRentalDto {
    val request = EndRentalDto(
        user_id = userId,
        station_end_id = stationStartId // si tienes stationEndId, cámbialo aquí
    )
    Log.d("RENTAL_REQUEST", "DTO enviado: $request")
    return request
}
