package com.example.sombriyakotlin

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration

import com.example.sombriyakotlin.data.location.GPSManager
import dagger.hilt.android.HiltAndroidApp
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.sombriyakotlin.worker.WeatherWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Inject


@HiltAndroidApp
class SombriYaKotlinApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()


    override fun onCreate() {
        super.onCreate()
        Log.d("EMPIEZAAPP", "si se inicio la app")
        GPSManager.initialize(this)
        createNotificationChannel(this)
        scheduleWeatherCheck(this)
        Log.d("EMPIEZAAPP", "Se llamo todo correctamente")
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
        val workRequest = OneTimeWorkRequestBuilder<WeatherWorker>().build()
        WorkManager.getInstance(context).enqueue(workRequest)
        Log.d("SEPROGRAMO", "✅ Se programó el WeatherWorker")
    }
}

    /*
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
}*/