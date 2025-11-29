package com.example.sombriyakotlin.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult

import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
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
    fun observeLocationFlow(): Flow<Location> = callbackFlow {
        var lastLocation: Location? = null
        // intenta enviar la última ubicación conocida (si la hay)
        fusedClient.lastLocation
            .addOnSuccessListener { last ->
                if (last != null) {
                    lastLocation = last
                    trySend(last).isSuccess
                }
            }

        val cb = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val loc = result.lastLocation ?: return
                val previous = lastLocation
                if (previous == null || loc.distanceTo(previous) > 10f) {
                    lastLocation = loc
                    trySend(loc).isSuccess
                }
            }
        }

        fusedClient.requestLocationUpdates(locationRequest, cb, Looper.getMainLooper())

        // Cuando se cancela la colección, quitar updates
        awaitClose {
            fusedClient.removeLocationUpdates(cb)
        }
    }.conflate() // evita backlog si UI no consume tan rápido
}
