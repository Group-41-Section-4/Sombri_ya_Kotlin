package com.example.sombriyakotlin

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.Configuration
import androidx.hilt.work.HiltWorkerFactory
import com.example.sombriyakotlin.data.location.GPSManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SombriYaKotlinApp : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)          // no molesta aunque el worker no use Hilt
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()

    private lateinit var appBgObserver: AppBackgroundObserver

    // app/src/main/java/com/example/sombriyakotlin/SombriYaKotlinApp.kt
    override fun onCreate() {
        super.onCreate()
        Log.d("APP", "App creada")

        // ✅ Inicializa el GPSManager ANTES de que cualquier VM/Screen intente usarlo
        GPSManager.initialize(this)

        createNotificationChannel()
        appBgObserver = AppBackgroundObserver(applicationContext)
        ProcessLifecycleOwner.get().lifecycle.addObserver(appBgObserver)
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ch = NotificationChannel(
                "weather_channel",
                "Clima",
                NotificationManager.IMPORTANCE_HIGH
            )
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(ch)
        }
    }
}
