package com.example.sombriyakotlin

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlin.random.Random

object NotificationHelper {

    private const val CHANNEL_ID = "default_channel_id"
    private const val CHANNEL_NAME = "Alertas de SombriYa"

    @SuppressLint("MissingPermission")
    fun showNotification(context: Context, title: String, message: String) {
        // 🔹 Crear el canal (solo una vez en Android 8.0+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canal para notificaciones de clima y rentas"
            }

            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        // 🔹 Intent al abrir la notificación
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        // 🔹 Construir la notificación
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // ✅ prioridad alta
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // 🔹 Mostrar la notificación (con ID único)
        NotificationManagerCompat.from(context)
            .notify(Random.nextInt(1000, 9999), builder.build())
    }
}
