package com.example.sombriyakotlin

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.sombriyakotlin.data.location.GPSManager
import dagger.hilt.android.HiltAndroidApp
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit


@HiltAndroidApp
class SombriYaKotlinApp : Application(){
    override fun onCreate() {
        super.onCreate()
        GPSManager.initialize(this)
        createNotificationChannel(this)
        scheduleWeatherCheck(this)
    }
    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "weather_channel",
                "Clima",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
    private fun scheduleWeatherCheck(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<WeatherWorker>(
            3, TimeUnit.HOURS // cada 3 horas
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "WeatherCheckWork",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }
}
