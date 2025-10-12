package com.example.sombriyakotlin.feature.history

import History
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.domain.usecase.rental.RentalUseCases
import com.example.sombriyakotlin.domain.usecase.user.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.max

// --- NUEVO MODELO DE UI SIMPLE PARA LAS CARDS ---
// Contiene SÓLO los campos necesarios para la tarjeta de historial.
data class HistoryUiItem(
    val date: String,          // e.g., "4 de octubre, 2025"
    val durationMinutes: Int,  // e.g., 1 (para "Duración: 1 minutos")
    val time: String,          // e.g., "07:33 PM"
    val id: String             // Añadimos el ID por si la tarjeta es clickeable
)
// ------------------------------------------------

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val rentalUseCases: RentalUseCases,
    private val userUseCases: UserUseCases
) : ViewModel() {

    // ** CAMBIO CLAVE: Usamos HistoryUiItem en el StateFlow **
    private val _history = MutableStateFlow<List<HistoryUiItem>>(emptyList())
    val history: StateFlow<List<HistoryUiItem>> = _history

    /* ------------------ utils de tiempo ------------------ */
    private fun parseIso(iso: String?): Date? {
        if (iso.isNullOrBlank()) return null
        val patterns = listOf(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "yyyy-MM-dd'T'HH:mm:ss"
        )
        for (p in patterns) {
            try {
                // Usamos Locale.ROOT para formatos ISO y gestionamos el 'Z' (UTC)
                val f = SimpleDateFormat(p, Locale.ROOT)
                if (p.endsWith("'Z'")) f.timeZone = TimeZone.getTimeZone("UTC")
                return f.parse(iso)
            } catch (_: ParseException) {}
        }
        return null
    }

    private fun formatDateEs(date: Date): String =
        SimpleDateFormat("d 'de' MMMM, yyyy", Locale("es", "CO")).format(date)

    private fun formatTime(date: Date): String =
        SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date)

    /* ------------------ API pública ------------------ */

    fun onScreenOpened() {
        Log.d("HistoryVM", "Pantalla de historial abierta → iniciando carga de datos")
        loadUserHistory()
    }

    /* ------------------ Carga del historial ------------------ */

    private fun loadUserHistory() {
        viewModelScope.launch {
            try {
                Log.d("HistoryVM", "Obteniendo usuario autenticado...")
                val user = userUseCases.getUserUseCase().first()
                if (user == null) {
                    Log.d("HistoryVM", "No se encontró usuario autenticado")
                    _history.value = emptyList()
                    return@launch
                }

                Log.d("HistoryVM", "Usuario encontrado: ${user.id}")
                Log.d("HistoryVM", "Consultando rentas del backend (status = 'completed')...")

                // 'rentals' es una lista de la clase History completa (el DTO)
                val rentals = rentalUseCases.getRentalsHystoryUserUseCase.invoke(user.id, status = "completed")

                Log.d("HistoryVM", "Rentas obtenidas: ${rentals.size}")

                val mapped = rentals.mapNotNull { r ->
                    val start = parseIso(r.startedAt)
                    if (start == null) {
                        Log.w("HistoryVM", "Renta ID ${r.id} omitida: startedAt es inválido.")
                        return@mapNotNull null
                    }

                    val durationMin = r.durationMinutes.takeIf { it >= 0 } ?: run {
                        val end = parseIso(r.endedAt)
                        if (end != null) max(0, ((end.time - start.time) / 60000L).toInt()) else 0
                    }

                    HistoryUiItem(
                        id = r.id,
                        date = formatDateEs(start),
                        durationMinutes = durationMin,
                        time = formatTime(start)
                    )
                }.sortedByDescending { h ->
                    parseIso("${h.date} ${h.time}")?.time ?: 0L
                }

                Log.d("HistoryVM", "Historial mapeado: ${mapped.size} elementos")

                _history.value = mapped
                Log.d("HistoryVM", "StateFlow actualizado con ${mapped.size} elementos")

            } catch (e: Exception) {
                Log.e("HistoryVM", "Error cargando historial", e)
                _history.value = emptyList()
            }
        }
    }
}