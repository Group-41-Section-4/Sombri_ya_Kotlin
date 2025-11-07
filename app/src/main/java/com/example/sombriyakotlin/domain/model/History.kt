import java.util.UUID

// domain/model/HistoryItem.kt

data class HistoryItem(
    val id: String = UUID.randomUUID().toString(),
    val date: String,
    val durationMinutes: Int,
    val time: String
)



data class History(
    // Campos que corresponden a RentalHistoryDto
    val id: String, // Cambiado de Int a String
    val userId: String? = null, // Asumiendo que userId no viene del DTO, lo hacemos opcional
    val startLat: Double? = null, // Corresponde a latitude
    val startLon: Double? = null, // Corresponde a longitude
    val startedAt: String, // Corresponde a start_time
    val endedAt: String? = null, // Corresponde a end_time
    val status: String,
    val durationMinutes: Int, // Corresponde a duration_minutes
    val distanceMeters: Double? = null, // Corresponde a distance_meters
    val authType: String, // Corresponde a auth_type
    val authAttempts: Int, // Corresponde a auth_attempts

    // Campos no incluidos en el DTO original, pero mantenidos como opcionales o con valores por defecto
    val stationStartId: String? = null,
    val paymentMethodId: String? = null
)