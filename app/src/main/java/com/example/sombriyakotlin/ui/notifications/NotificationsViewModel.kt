package com.example.sombriyakotlin.ui.notifications

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.NotificationHelper
import com.example.sombriyakotlin.domain.model.Notification
import com.example.sombriyakotlin.domain.model.NotificationType
import com.example.sombriyakotlin.domain.repository.WeatherRepository
import com.example.sombriyakotlin.domain.usecase.ObserveConnectivityUseCase
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
import dagger.hilt.android.qualifiers.ApplicationContext


@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val rentalUseCases: RentalUseCases,
    private val userUseCases: UserUseCases,
    observeConnectivity: ObserveConnectivityUseCase,
    @ApplicationContext private val context: Context   // 👈 agregado

) : ViewModel() {

    val isConnected: StateFlow<Boolean> = observeConnectivity()


    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications

    /* ------------------ utils de tiempo ------------------ */
    private fun nowLabel(): String =
        SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())

    private fun nextId(prefix: String) =
        "$prefix-${System.currentTimeMillis()}-${Random.nextInt(0, 999)}"

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

    fun clearAll() {
        _notifications.value = emptyList()
    }

    fun removeById(id: String) {
        _notifications.value = _notifications.value.filterNot { it.id == id }
    }

    fun onScreenOpened(context: Context, lastLocation: Location? = null) {
        loadUserRentalsAsNotifications()

        // Uniandes por defecto si no nos pasan ubicación
        val defaultLocation = Location("").apply {
            latitude = 4.6015
            longitude = -74.0662
        }

        val loc = lastLocation ?: defaultLocation
        checkWeatherAt(context, loc)
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
                        message = if (!isConnected.value) "No hay conexión a internet" else e.message ?: "Error desconocido.",
                        time = nowLabel()
                    )
                )
            }
        }
    }

    /** ----------------- CLIMA ----------------- */

    fun checkWeatherAt(context: Context, location: Location) {
        viewModelScope.launch {
            Log.d("JULIANICEN","xd")
            if (!isConnected.value) {
                val newNotif = Notification(
                    id = nextId("w"),
                    type = NotificationType.WEATHER,
                    title = "No hay internet para el clima",
                    message = "Revise la conexión a internet",
                    time = nowLabel()
                )
                _notifications.value = _notifications.value + newNotif

            }else {
                val pop = weatherRepository
                    .getFirstPopPercent(location.latitude, location.longitude)
                    ?: return@launch
                Log.d("JULIANICEN", "MUERE")
                if (pop > 30) {
                    Log.d("JULIANICEN", "QUE PASO")

                    val newNotif = Notification(
                        id = nextId("w"),
                        type = NotificationType.WEATHER,
                        title = "Alerta de Lluvia",
                        message = "Hay $pop% de probabilidad de lluvia en las próximas horas.",
                        time = nowLabel()
                    )
                    _notifications.value = _notifications.value + newNotif

                    //  Notificación del sistema
                    NotificationHelper.showNotification(
                        context,
                        title = newNotif.title,
                        message = newNotif.message
                    )
                } else {
                    Log.d("JULIANICEN", "QUE PASO")
                    val newNotif = Notification(
                        id = nextId("w"),
                        type = NotificationType.WEATHER,
                        title = "Riesgo bajo de Lluvia",
                        message = "Hay $pop% de probabilidad de lluvia en las próximas horas.",
                        time = nowLabel()
                    )
                    _notifications.value = _notifications.value + newNotif

                    // 🔔 Notificación del sistema
                    NotificationHelper.showNotification(
                        context,
                        title = newNotif.title,
                        message = newNotif.message
                    )

                }
            }
        }
    }

    /** ----------------- ACTUALIZACIÓN AUTOMÁTICA ----------------- */

    private var autoJob: Job? = null

    fun bindLocationAuto(
        context: Context,
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
                checkWeatherAt(context, loc)
            }
        }
    }

    fun unbindLocationAuto() {
        autoJob?.cancel()
        autoJob = null
    }
}
