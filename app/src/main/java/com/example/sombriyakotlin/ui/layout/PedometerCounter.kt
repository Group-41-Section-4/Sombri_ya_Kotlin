package com.example.sombriyakotlin.ui.layout


import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel

//@Preview
@Composable
fun PedometerCounter(
    viewModel: PedometerViewModel = hiltViewModel()
){
    val context = LocalContext.current
    val count by viewModel.count.collectAsState()
    val permission = Manifest.permission.ACTIVITY_RECOGNITION

    val running by viewModel.isRunning.collectAsState()


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startPedometer()
        } else {
            Toast.makeText(context, "Permiso denegado para el podómetro", Toast.LENGTH_SHORT).show()
        }
    }
    Log.d("PedometerCounter", "count: $count")

    // Solicitar permiso al mostrar el composable
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, permission)
            == PackageManager.PERMISSION_GRANTED) {
            //running = true
            //viewModel.startPedometer()
        } else {
            launcher.launch(permission)
        }
    }

    if (running){
        Text(text = "Pasos: $count", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.fillMaxWidth())
    }
    else {
        //Button({ running = true; viewModel.startPedometer() }) { }
    }
}