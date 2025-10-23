package com.example.sombriyakotlin

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.sombriyakotlin.data.location.GPSManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SombriYaKotlinApp : Application(){
    override fun onCreate() {
        super.onCreate()
        GPSManager.initialize(this)
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "default_channel_id"
            val channelName = "Notificaciones generales"
            val channelDescription = "Canal para las notificaciones del sistema"
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
