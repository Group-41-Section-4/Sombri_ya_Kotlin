package com.example.sombriyakotlin

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.sombriyakotlin.ui.navigation.AppNavigation
import com.example.sombriyakotlin.ui.theme.SombriYaKotlinTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("QUEPASO","www")
        askNotificationPermission()
        askLocationPermission()
        super.onCreate(savedInstanceState)



        setContent {
            val navController = rememberNavController()
            SombriYaKotlinTheme {
                AppNavigation(navController = navController, false)
            }
        }
    }

    /**
     * Solicita el permiso POST_NOTIFICATIONS en Android 13+
     */
    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    private fun askLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    /**
     * Resultado de la solicitud del permiso
     */
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                // El usuario rechazó el permiso: podrías mostrar un mensaje o ignorar
            }
        }


}
