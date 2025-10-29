package com.example.sombriyakotlin.feature.notifications.worker

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.sombriyakotlin.data.worker.WeatherWorker
import java.util.concurrent.TimeUnit

object WeatherWorkScheduler {

    private const val UNIQUE_NAME = "weather_periodic"

    fun enqueuePeriodic(
        context: Context,
        lat: Double,
        lon: Double
    ) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val req = PeriodicWorkRequestBuilder<WeatherWorker>(
            15, TimeUnit.MINUTES // mínimo permitido por WorkManager
        )
            .setInputData(workDataOf("lat" to lat, "lon" to lon))
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            UNIQUE_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            req
        )

        Log.d("WEATHER_SCHED", "→ enqueuePeriodic id=${req.id} ($lat,$lon)")
    }

    fun cancel(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(UNIQUE_NAME)
        Log.d("WEATHER_SCHED", "→ cancel periodic")
    }
}
