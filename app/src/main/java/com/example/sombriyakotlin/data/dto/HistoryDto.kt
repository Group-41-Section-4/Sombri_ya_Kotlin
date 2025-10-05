// data/dto/HistoryDto.kt
package com.example.sombriyakotlin.data.dto

data class HistoryDto(
    val id: String,              // UUID del rental (no lo usas en UI, pero útil para debug)
    val startTime: String,       // viene como start_time del backend
    val endTime: String?,        // viene como end_time
    val status: String,          // "completed"
    val durationMinutes: Int,    // viene como duration_minutes
    val distanceMeters: Int?,    // opcional
    val authType: String,        // "qr" / "nfc"
    val authAttempts: Int        // 1, 2, ...
)
