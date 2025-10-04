// app/src/main/java/com/example/sombriyakotlin/feature/notifications/NotificationsViewModel.kt
package com.example.sombriyakotlin.feature.notifications

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sombriyakotlin.domain.model.Notification
import com.example.sombriyakotlin.domain.model.NotificationType
import com.example.sombriyakotlin.domain.repository.WeatherRepository
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
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications

    private fun nowLabel(): String =
        SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())

    private fun nextId(prefix: String) =
        "$prefix-${System.currentTimeMillis()}-${Random.nextInt(0, 999)}"

    /** ----------------- API pública para la UI ----------------- */

    fun clearAll() { _notifications.value = emptyList() }

    fun removeById(id: String) {
        _notifications.value = _notifications.value.filterNot { it.id == id }
    }

    /** Notificación por SUSCRIPCIÓN (para semillas / demo) */
    fun addSubscription(message: String) {
        _notifications.value = _notifications.value + Notification(
            id = nextId("s"),
            type = NotificationType.SUBSCRIPTION,
            title = "Suscripción",
            message = message,
            time = nowLabel()
        )
    }

    /** Notificación por ALQUILER (para semillas / demo) */
    fun addRental(message: String) {
        _notifications.value = _notifications.value + Notification(
            id = nextId("r"),
            type = NotificationType.RENTAL,
            title = "Recordatorio",
            message = message,
            time = nowLabel()
        )
    }

    /**
     * Llamar desde la Screen en `LaunchedEffect(Unit)`:
     * - Si la lista está vacía, agrega 2 notificaciones por defecto
     * - Si hay ubicación, hace UNA consulta a la Weather API (para comprobar)
     */
    fun onScreenOpened(lastLocation: Location?) {
        if (_notifications.value.isEmpty()) {
            addSubscription("Tu plan mensual se renovó correctamente.")
            addRental("Tienes una sombrilla en alquiler. ¡No olvides devolverla!")
        }
        lastLocation?.let { checkWeatherAt(it) }
    }

    /** ----------------- CLIMA ----------------- */

    /** Consulta una vez la Weather API con la ubicación dada */
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
            /*
            else { // Solo para debug
                println("DEBUG POP: $pop")
                _notifications.value = _notifications.value + Notification(
                    id = nextId("w"),
                    type = NotificationType.WEATHER,
                    title = "Prueba API",
                    message = "Probabilidad de lluvia: $pop%",
                    time = nowLabel()
                )
            }
*/
        }
    }

    /** Modo automático: llama periódicamente cuando cambia la ubicación */
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
