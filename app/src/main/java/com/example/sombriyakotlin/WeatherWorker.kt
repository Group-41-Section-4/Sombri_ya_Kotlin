package com.example.sombriyakotlin

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.runBlocking
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.sombriyakotlin.domain.model.Notification
import com.example.sombriyakotlin.domain.model.NotificationType
import com.example.sombriyakotlin.domain.repository.WeatherRepository
import com.google.android.gms.location.LocationServices
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.random.Random

@HiltWorker
class WeatherWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val weatherRepository: WeatherRepository
) : CoroutineWorker(appContext, workerParams) {

    private fun nowLabel(): String =
        SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())

    private fun nextId(prefix: String) =
        "$prefix-${System.currentTimeMillis()}-${Random.nextInt(0, 999)}"

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        Log.d("SANTAFE", "Entró a doWork()")
        try {
            Log.d("SANTAFE", "✅ Worker ejecutándose")

            val location = getLastKnownLocation(applicationContext)
            if (location == null) {
                Log.e("SANTAFE", "❌ No se pudo obtener la ubicación")
                return@withContext Result.retry()
            }

            val pop = weatherRepository.getFirstPopPercent(location.latitude, location.longitude)
            if (pop == null) {
                Log.e("SANTAFE", "❌ No se obtuvo POP del repositorio")
                return@withContext Result.retry()
            }

            Log.d("SANTAFE", "🌦 Probabilidad de lluvia: $pop%")

            if (pop > 30) {
                val newNotif = Notification(
                    id = nextId("w"),
                    type = NotificationType.WEATHER,
                    title = "Alerta de lluvia",
                    message = "Hay un $pop% de probabilidad de lluvia en las próximas horas.",
                    time = nowLabel()
                )

                NotificationHelper.showNotification(
                    applicationContext,
                    title = newNotif.title,
                    message = newNotif.message
                )
            }

            // Reprogramar el siguiente chequeo dentro de 5 minutos
            val nextWork = OneTimeWorkRequestBuilder<WeatherWorker>()
                .setInitialDelay(5, TimeUnit.MINUTES)
                .build()

            WorkManager.getInstance(applicationContext).enqueue(nextWork)
            Log.d("SANTAFE", "🔁 Siguiente ejecución programada")

            Result.success()
        } catch (e: Exception) {
            Log.d("SANTAFE", "💥 Error en worker: ${e.message}", e)
            Result.retry()
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getLastKnownLocation(context: Context): Location? {
        val fused = LocationServices.getFusedLocationProviderClient(context)
        return try {
            fused.lastLocation.await()
        } catch (e: Exception) {
            Log.d("SANTAFE", "⚠️ Error obteniendo ubicación: ${e.message}")
            null
        }
    }
}
