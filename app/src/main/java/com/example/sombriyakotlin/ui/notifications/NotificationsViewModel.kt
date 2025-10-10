package com.example.sombriyakotlin.feature.notifications

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.domain.model.Notification
import com.example.sombriyakotlin.domain.model.NotificationType
import com.example.sombriyakotlin.domain.repository.WeatherRepository
import com.example.sombriyakotlin.domain.usecase.rental.RentalUseCases
import com.example.sombriyakotlin.domain.usecase.user.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val rentalUseCases: RentalUseCases,
    private val userUseCases: UserUseCases
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications

    /* ------------------ utils de tiempo ------------------ */
    private fun nowLabel(): String =
        SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())

    private fun nextId(prefix: String) =
        "$prefix-${System.currentTimeMillis()}-${Random.nextInt(0, 999)}"

    // Intenta formatear ISO8601 (p. ej. "2025-10-04T19:10:02.922Z") a "hh:mm a"
    private fun formatIsoToHourMinute(iso: String?): String {
        if (iso.isNullOrBlank()) return nowLabel()
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            val date = parser.parse(iso) ?: return nowLabel()
            val localFmt = SimpleDateFormat("hh:mm a", Locale.getDefault())
            localFmt.format(date)
        } catch (_: Exception) {
            nowLabel()
        }
    }

    /** ----------------- API pública para la UI ----------------- */

    fun clearAll() { _notifications.value = emptyList() }

    fun removeById(id: String) {
        // fix: estaba filtrando al revés
        _notifications.value = _notifications.value.filterNot { it.id == id }
    }

    fun onScreenOpened(lastLocation: Location? = null) {
        loadUserRentalsAsNotifications()

        // Uniandes por defecto si no nos pasan ubicación
        val defaultLocation = Location("").apply {
            latitude = 4.6015
            longitude = -74.0662
        }
        checkWeatherAt(defaultLocation)
    }

    /** ----------------- ALQUILERES DEL BACKEND ----------------- */

    private fun loadUserRentalsAsNotifications() {
        viewModelScope.launch {
            try {
                val user = userUseCases.getUserUseCase().first()

                if (user != null) {
                    Log.d("RENTALS", "Cargando rentas del usuario...")
                    val rentals = rentalUseCases.getRentalsUserUseCase.invoke(user.id, status = "ongoing")
                    Log.d("RENTALS", "Rentas obtenidas: ${rentals.size}")

                    val rentalNotifications = rentals.map { rental ->
                        val startedLabel = formatIsoToHourMinute(rental.startedAt)
                        val status = rental.status?.lowercase(Locale.getDefault()) ?: ""

                        when (status) {
                            "ongoing" -> Notification(
                                id = nextId("reminder"),
                                type = NotificationType.RENTAL,
                                title = "Recordatorio",
                                message = "Tienes una sombrilla en alquiler. ¡No olvides devolverla!",
                                time = startedLabel
                            )
                            "created" -> Notification(
                                id = nextId("rent"),
                                type = NotificationType.RENTAL,
                                title = "Preparando alquiler",
                                message = "Estamos confirmando tu alquiler en ${rental.stationStartId}.",
                                time = startedLabel
                            )
                            "completed" -> Notification(
                                id = nextId("rent"),
                                type = NotificationType.RENTAL,
                                title = "Gracias por devolverla",
                                message = "Tu alquiler ha finalizado correctamente.",
                                time = startedLabel
                            )
                            else -> Notification(
                                id = nextId("rent"),
                                type = NotificationType.RENTAL,
                                title = "Recordatorio",
                                message = "Tienes una sombrilla en alquiler.",
                                time = startedLabel
                            )
                        }
                    }

                    _notifications.value = _notifications.value + rentalNotifications
                } else {
                    _notifications.value = listOf(
                        Notification(
                            id = nextId("error"),
                            type = NotificationType.RENTAL,
                            title = "Error de sesión",
                            message = "No se pudo obtener el usuario autenticado.",
                            time = nowLabel()
                        )
                    )
                }
            } catch (e: Exception) {
                _notifications.value = listOf(
                    Notification(
                        id = nextId("error"),
                        type = NotificationType.RENTAL,
                        title = "Error al cargar rentas",
                        message = e.message ?: "Error desconocido.",
                        time = nowLabel()
                    )
                )
            }
        }
    }

    /** ----------------- CLIMA ----------------- */

    fun checkWeatherAt(location: Location) {
        viewModelScope.launch {
            val pop = weatherRepository
                .getFirstPopPercent(location.latitude, location.longitude)
                ?: return@launch

            if (pop > 30) {
                _notifications.value = _notifications.value + Notification(
                    id = nextId("w"),
                    type = NotificationType.WEATHER,
                    title = "Alerta de Lluvia",
                    message = "Hay $pop% de probabilidad de lluvia en las próximas horas.",
                    time = nowLabel()
                )
            }
            else{

                    _notifications.value = _notifications.value + Notification(
                        id = nextId("w"),
                        type = NotificationType.WEATHER,
                        title = "Alerta de Lluvia",
                        message = "Hay $pop% de probabilidad de lluvia en las próximas horas.",
                        time = nowLabel()
                    )

            }
        }
    }

    /** ----------------- ACTUALIZACIÓN AUTOMÁTICA ----------------- */

    private var autoJob: Job? = null

    fun bindLocationAuto(
        locationFlow: Flow<Location?>,
        minMinutesBetweenCalls: Long = 60
    ) {
        if (autoJob?.isActive == true) return
        autoJob = viewModelScope.launch {
            var lastCallAt = 0L
            locationFlow.filterNotNull().collect { loc ->
                val now = System.currentTimeMillis()
                if (now - lastCallAt < minMinutesBetweenCalls * 60_000) return@collect
                lastCallAt = now
                checkWeatherAt(loc)
            }
        }
    }

    fun unbindLocationAuto() {
        autoJob?.cancel()
        autoJob = null
    }
}
