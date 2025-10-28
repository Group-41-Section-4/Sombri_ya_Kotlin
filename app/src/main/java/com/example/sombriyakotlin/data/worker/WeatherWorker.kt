package com.example.sombriyakotlin.data.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.sombriyakotlin.BuildConfig
import com.example.sombriyakotlin.NotificationHelper
import com.example.sombriyakotlin.data.datasource.*
import com.example.sombriyakotlin.data.datasource.stations.StationsCacheLocalDataSource
import com.example.sombriyakotlin.data.repository.StationsCacheRepository
import com.example.sombriyakotlin.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import kotlin.math.*

class WeatherWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    private val client by lazy { OkHttpClient() }

    override suspend fun doWork(): Result {
        Log.d("WeatherWorker", "⚙️ doWork() START")

        val lat = inputData.getDouble("lat", Double.NaN)
        val lon = inputData.getDouble("lon", Double.NaN)
        Log.d("WeatherWorker", "inputData lat=$lat lon=$lon")

        if (lat.isNaN() || lon.isNaN()) {
            Log.e("WeatherWorker", " Faltan lat/lon en inputData")
            return Result.failure()
        }

        val baseUrl = (BuildConfig.OWM_API_URL ?: "").trimEnd('/')
        val apiKey = BuildConfig.OWM_API_KEY ?: ""
        if (baseUrl.isBlank() || apiKey.isBlank()) {
            Log.e("WeatherWorker", " OWM_API_URL / OWM_API_KEY vacíos en BuildConfig")
            return Result.failure()
        }

        val url = "$baseUrl/forecast?lat=$lat&lon=$lon&appid=$apiKey&lang=es&units=metric"
        Log.d("WeatherWorker", " URL: $url")

        return try {
            // POP
            val pop = withContext(Dispatchers.IO) {
                val req = Request.Builder().url(url).get().build()
                client.newCall(req).execute().use { resp ->
                    if (!resp.isSuccessful) {
                        Log.e("WeatherWorker", "HTTP ${resp.code}")
                        return@withContext null
                    }
                    val body = resp.body?.string()
                    if (body.isNullOrBlank()) return@withContext null
                    val json = JSONObject(body)
                    val list = json.optJSONArray("list") ?: return@withContext null
                    if (list.length() == 0) return@withContext null
                    val first = list.getJSONObject(0)
                    val pop0to1 = first.optDouble("pop", 0.0)
                    (pop0to1 * 100).toInt()
                }
            }

            if (pop == null) {
                Log.e("WeatherWorker", "No se pudo obtener POP del forecast")
                return Result.retry()
            }

            Log.d("WeatherWorker", " POP=$pop%")

            //  Cargar estaciones cacheadas con TTL
            val cacheRepo = StationsCacheRepository(
                StationsCacheLocalDataSource(applicationContext.dataStore)
            )
            val cachedStations = cacheRepo.getCachedStationsFreshOrEmpty()

            //  Buscar la estación más cercana
            val nearest = cachedStations.minByOrNull {
                distanceMeters(lat, lon, it.latitude, it.longitude)
            }

            //  Mostrar notificación personalizada
            if (pop < 30) {
                val message = if (nearest != null)
                    "Hay $pop% de probabilidad de lluvia. Alquila en ${nearest.name}."
                else
                    "Hay $pop% de probabilidad de lluvia en tu zona. Revisa las estaciones disponibles."

                NotificationHelper.showNotification(
                    applicationContext,
                    title = "Alerta de lluvia ☔",
                    message = message
                )
                Log.d("WeatherWorker", "🔔 Notificación mostrada: $message")
            } else {
                Log.d("WeatherWorker", "🌤 POP bajo, no se notifica")
            }

            Result.success()

        } catch (e: Exception) {
            Log.e("WeatherWorker", "❌ Excepción en doWork", e)
            Result.retry()
        }
    }

    /** Calcula distancia entre dos coordenadas (Haversine) */
    private fun distanceMeters(aLat: Double, aLon: Double, bLat: Double, bLon: Double): Double {
        val R = 6371000.0
        val dLat = Math.toRadians(bLat - aLat)
        val dLon = Math.toRadians(bLon - aLon)
        val lat1 = Math.toRadians(aLat)
        val lat2 = Math.toRadians(bLat)
        val h = sin(dLat / 2).pow(2.0) + cos(lat1) * cos(lat2) * sin(dLon / 2).pow(2.0)
        return 2 * R * asin(sqrt(h))
    }
}
