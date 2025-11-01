package com.example.sombriyakotlin

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.workDataOf
import com.example.sombriyakotlin.data.worker.WeatherWorker
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
                //cardRent(navController = rememberNavController())
                //paymentMethopdsCard(navController)
                //HistoryScreen(navController =navController )
                AppNavigation(navController = navController, false)
            }
        }
        //testWeatherWorker() //Prueba para nmo esperar el repogramamiento y  eso

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
    private fun testWeatherWorker() {
        val req = OneTimeWorkRequestBuilder<WeatherWorker>()
            .setInputData(workDataOf("lat" to 4.65, "lon" to -74.05)) // Bogotá
            .build()

        val wm = androidx.work.WorkManager.getInstance(this)
        wm.enqueue(req)
        Log.d("TEST_WORKER", " Enqueued WeatherWorker id=${req.id}")

        // Observa el estado en tiempo real (ENQUEUED -> RUNNING -> SUCCEEDED/FAILED/RETRY)
        wm.getWorkInfoByIdLiveData(req.id).observe(this) { wi ->
            if (wi != null) {
                Log.d(
                    "WM_OBS",
                    "id=${wi.id} state=${wi.state} runAttempt=${wi.runAttemptCount} tags=${wi.tags}"
                )
                if (wi.outputData.keyValueMap.isNotEmpty()) {
                    wi.outputData.keyValueMap.forEach { (k, v) ->
                        Log.d("WM_OBS", "output[$k]=$v")
                    }
                }
            }
        }
    }



}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SombriYaKotlinTheme {
        Greeting("Android")
    }
}
