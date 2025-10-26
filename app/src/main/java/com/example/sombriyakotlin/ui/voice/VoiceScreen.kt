package com.example.sombriyakotlin.ui.voice

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sombriyakotlin.R

@Composable
fun VoiceScreen(
    navController: NavController,
    viewModel: VoiceViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var hasStarted by remember { mutableStateOf(false) }

    // 🔐 Launcher para pedir permiso de micrófono
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            Log.d("Voice", "🎤 Permiso concedido, empezando a escuchar...")
            viewModel.startListening(
                context = context,
                onFinished = { hasStarted = false },
                onNavigateToRent = {
                    navController.navigate("rent") {
                        popUpTo("voice") { inclusive = true }
                    }
                }
            )
            hasStarted = true
        } else {
            Log.e("Voice", "🚫 Permiso de micrófono denegado")
        }
    }

    // 🔄 Lanzar automáticamente al entrar
    LaunchedEffect(Unit) {
        val permissionCheck = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        )

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            viewModel.startListening(
                context = context,
                onFinished = { hasStarted = false },
                onNavigateToRent = {
                    navController.navigate("rent") {
                        popUpTo("voice") { inclusive = true }
                    }
                }
            )
            hasStarted = true
        } else {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    // 🖼️ UI simple mientras escucha
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.umbrella_fill), // ⚠️ pon tu recurso aquí
            contentDescription = "Micrófono",
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (hasStarted) "Escuchando..." else "Procesando...",
        )
    }
}
