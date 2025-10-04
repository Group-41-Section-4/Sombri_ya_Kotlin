package com.example.sombriyakotlin.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate

/**
 * Singleton que expone un Flow<Location>.
 * - Inicializar en Application: GPSManager.initialize(appContext)
 * - Antes de recolectar el flow, asegúrate de tener permisos de ubicación.
 */
object GPSManager {

    private lateinit var fusedClient: FusedLocationProviderClient

    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 10_000L
        fastestInterval = 5_000L
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    fun initialize(context: Context) {
        fusedClient = LocationServices.getFusedLocationProviderClient(context.applicationContext)
    }

    /**
     * Flow frío: al mostrar collectors, comienza requestLocationUpdates.
     *
     * IMPORTANT: verifica permisos antes de collectar este Flow.
     */
    @SuppressLint("MissingPermission") // quien colecciona debe comprobar permisos
    fun observeLocationFlow() = callbackFlow<Location> {
        // intenta enviar la última ubicación conocida (si la hay)
        fusedClient.lastLocation
            .addOnSuccessListener { last -> last?.let { trySend(it).isSuccess } }

        val cb = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { trySend(it).isSuccess }
            }
        }

        fusedClient.requestLocationUpdates(locationRequest, cb, Looper.getMainLooper())

        // Cuando se cancela la colección, quitar updates
        awaitClose {
            fusedClient.removeLocationUpdates(cb)
        }
    }.conflate() // evita backlog si UI no consume tan rápido
}
