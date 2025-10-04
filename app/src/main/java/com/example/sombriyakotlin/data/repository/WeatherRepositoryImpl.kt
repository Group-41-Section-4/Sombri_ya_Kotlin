// app/src/main/java/com/example/sombriyakotlin/feature/notifications/data/WeatherRepositoryImpl.kt
package com.example.sombriyakotlin.feature.notifications.data

import com.example.sombriyakotlin.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiKey: String
) : WeatherRepository {

    private val client = OkHttpClient()

    override suspend fun getFirstPopPercent(lat: Double, lon: Double): Int? =
        withContext(Dispatchers.IO) {
            val url = "https://api.openweathermap.org/data/2.5/forecast" +
                    "?lat=$lat&lon=$lon&appid=$apiKey&lang=es&units=metric"

            val req = Request.Builder().url(url).get().build()
            client.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) return@use null
                val body = resp.body?.string() ?: return@use null // OkHttp 4.x
                val json = JSONObject(body)
                val list = json.optJSONArray("list") ?: return@use null
                if (list.length() == 0) return@use null
                val first = list.getJSONObject(0)
                val pop = first.optDouble("pop", 0.0) // 0..1
                (pop * 100).toInt()
            }
        }
}
