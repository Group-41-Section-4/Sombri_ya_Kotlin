package com.example.sombriyakotlin.data.dto

import History
import android.util.Log
import com.example.sombriyakotlin.domain.model.Rental

/* ------------------ DTOs (nullables para reflejar el backend) ------------------ */



import com.google.gson.annotations.SerializedName

data class RentalDtov2(
    @SerializedName("id") val id: String,
    @SerializedName("start_time") val startTime: String,
    @SerializedName("end_time") val endTime: String?,
    @SerializedName("status") val status: String,
    @SerializedName("duration_minutes") val durationMinutes: Int?,
    @SerializedName("distance_meters") val distanceMeters: Double?,
    @SerializedName("auth_type") val authType: String,
    @SerializedName("auth_attempts") val authAttempts: Int?,
    @SerializedName("user") val user: UserDtov2,
    @SerializedName("umbrella") val umbrella: UmbrellaDto?,
    @SerializedName("start_station") val startStation: StationDtov2?,
    @SerializedName("end_station") val endStation: StationDtov2?
)

data class UserDtov2(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("biometric_enabled") val biometricEnabled: Boolean?,
    @SerializedName("created_at") val createdAt: String?
    // puedes añadir los demás campos si los vas a usar
)

data class UmbrellaDto(
    @SerializedName("id") val id: String,
    @SerializedName("state") val state: String,
    @SerializedName("last_maintenance_at") val lastMaintenanceAt: String?
)

data class StationDtov2(
    @SerializedName("id") val id: String,
    @SerializedName("description") val description: String?,
    @SerializedName("place_name") val placeName: String?,
    @SerializedName("capacity") val capacity: Int?,
    @SerializedName("latitude") val latitude: String?,
    @SerializedName("longitude") val longitude: String?,
    @SerializedName("location") val location: LocationDto?,
)

data class LocationDto(
    @SerializedName("type") val type: String?,
    @SerializedName("coordinates") val coordinates: List<Double>?
)

data class ImageDto(
    @SerializedName("type") val type: String?,
    @SerializedName("data") val data: String?
)



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
data class RentalHistoryDto(
    // Parámetros originales
    val latitude: Double? = null,
    val longitude: Double? = null,

    // Parámetros añadidos
    val id: String,
    val start_time: String,
    val end_time: String,
    val status: String,
    val duration_minutes: Int,
    val distance_meters: Double? = null, // Usamos Double? ya que el valor es null
    val auth_type: String,
    val auth_attempts: Int
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
    return request
}

// Dominio -> DTO de fin de renta
fun Rental.toEndDto(): EndRentalDto {
    Log.d("RENT", "Response: $this")
    val request = EndRentalDto(
        user_id = userId,
        station_end_id = stationStartId
    )
    return request
}
fun RentalHistoryDto.toDomain(): History {
    return History(
        id = this.id,
        startLat = this.latitude,
        startLon = this.longitude,
        startedAt = this.start_time,
        endedAt = this.end_time,
        status = this.status,
        durationMinutes = this.duration_minutes,
        distanceMeters = this.distance_meters,
        authType = this.auth_type,
        authAttempts = this.auth_attempts,

        // Campos que no vienen del DTO y usan valores por defecto/null
        userId = null,
        stationStartId = null,
        paymentMethodId = null
    )
}


fun RentalDtov2.toDomainRental(): Rental {
    return Rental(
        id = 0, // tu backend envía un UUID (String), tu modelo es Int;
        // si luego cambias el dominio a String, aquí lo mapeas.
        userId = user.id,
        stationStartId = startStation?.id.orEmpty(),
        paymentMethodId = null, // el endpoint no envía método de pago aún
        startLat = startStation?.latitude?.toDoubleOrNull() ?: 0.0,
        startLon = startStation?.longitude?.toDoubleOrNull() ?: 0.0,
        authType = authType,
        status = status,
        startedAt = startTime,
        endedAt = endTime
    )
}

