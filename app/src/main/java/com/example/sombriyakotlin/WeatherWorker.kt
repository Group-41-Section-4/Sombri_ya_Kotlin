package com.example.sombriyakotlin

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.runBlocking
import android.util.Log
import com.example.sombriyakotlin.domain.repository.WeatherRepository
import com.example.sombriyakotlin.domain.usecase.weather.WeatherUseCases
import com.example.sombriyakotlin.feature.notifications.NotificationsViewModel

class WeatherWorker(
    private val context: Context,
    workerParams: WorkerParameters,
    private val weatherRepository: WeatherRepository
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        return try {
            runBlocking {
                // Aquí llamas a tu función de verificación del clima
                Log.d("WAHAPEN","suspecy")
                val pop = weatherRepository
                    .getFirstPopPercent(location.latitude, location.longitude)
                    ?: return@launch
                Log.d("WAHAPEN","wtffffffffff")
                if (rainExpected) {
                    NotificationHelper.showNotification(
                        context = context,
                        title = "Clima",
                        message = "Se espera lluvia. Recuerda llevar tu sombrilla ☔"
                    )
                } else {
                    Log.d("WeatherWorker", "No se espera lluvia")
                }
            }
            Result.success()
        } catch (e: Exception) {
            Log.e("WeatherWorker", "Error ejecutando checkWeather", e)
            Result.retry()
        }
    }

}
