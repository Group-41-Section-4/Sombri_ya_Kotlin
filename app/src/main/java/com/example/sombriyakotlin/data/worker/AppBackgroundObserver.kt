package com.example.sombriyakotlin

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.sombriyakotlin.feature.notifications.worker.WeatherWorkScheduler
import com.google.android.gms.location.LocationServices

class AppBackgroundObserver(
    private val appContext: Context
) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        // App en foreground → cancelamos periódicos
        Log.d("BG_OBS", "→ foreground: cancel periodic")
        WeatherWorkScheduler.cancel(appContext)
    }

    override fun onStop(owner: LifecycleOwner) {
        // App en background → encolamos periódicos con última ubicación disponible
        Log.d("BG_OBS", "→ background: schedule periodic")

        // 1) Si no hay permiso, no programamos
        val hasFine = ContextCompat.checkSelfPermission(
            appContext, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (!hasFine) {
            Log.w("BG_OBS", "No ACCESS_FINE_LOCATION; no se programa")
            return
        }

        // Intentamos usar la última ubicación conocida (no bloquea UI)
        val fused = LocationServices.getFusedLocationProviderClient(appContext)
        fused.lastLocation
            .addOnSuccessListener { loc ->
                val lat = loc?.latitude ?: 4.65      // Bogotá fallback
                val lon = loc?.longitude ?: -74.05
                WeatherWorkScheduler.enqueuePeriodic(appContext, lat, lon)
            }
            .addOnFailureListener {
                // Si falla, programa con fallback
                WeatherWorkScheduler.enqueuePeriodic(appContext, 4.65, -74.05)
            }
    }
}
