package com.example.sombriyakotlin.domain.usecase.weather

import android.content.Context
import android.location.Location
import android.util.Log
import com.example.sombriyakotlin.NotificationHelper
import com.example.sombriyakotlin.domain.model.Notification
import com.example.sombriyakotlin.domain.model.NotificationType
import com.example.sombriyakotlin.domain.repository.WeatherRepository

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class CheckWeatherUseCase (
    private val weatherRepository: WeatherRepository
)
{
    private fun nextId(prefix: String) =
        "$prefix-${System.currentTimeMillis()}-${Random.nextInt(0, 999)}"
    private fun nowLabel(): String =
        SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
    suspend fun checkWeatherAt(context: Context, location: Location) {


        val defaultLocation = Location("").apply {
            latitude = 4.6015
            longitude = -74.0662
        }
        //--------------------- LA LOCATION ESTA HARDCODE ---------------------
        val pop = weatherRepository.getFirstPopPercent(defaultLocation.latitude, defaultLocation.longitude)
            ?: return


        Log.d("WeatherChecker", "Probabilidad de lluvia: $pop%")

        if (pop > 30) {
            val newNotif = Notification(
                id = nextId("w"),
                type = NotificationType.WEATHER,
                title = "Alerta de Lluvia",
                message = "Hay $pop% de probabilidad de lluvia en las próximas horas.",
                time = nowLabel()
            )

            NotificationHelper.showNotification(
                context,
                title = newNotif.title,
                message = newNotif.message
            )

            Log.d("WeatherChecker", "Notificación enviada por lluvia")
        }
    }
}
