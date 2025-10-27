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
import androidx.navigation.NavHostController
import com.example.sombriyakotlin.R
import com.example.sombriyakotlin.ui.layout.AppLayout

@Composable
fun VoiceScreen(
    navController: NavController,
    navHostController: NavHostController,
    viewModel: VoiceViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var hasStarted by remember { mutableStateOf(false) }

    // Launcher para pedir permiso de micrófono
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            Log.d("Voice", "PERSMISO VOXZZZZZZZZZZZZ")
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
            Log.e("Voice", "LO NEGAROOOOOOOOOOON")
        }
    }

    // al iniciar'¿
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

    // 🔹 Usamos AppLayout para mantener top bar, bottom bar y drawer
    AppLayout(
        navController = navController,
        navHostController = navHostController
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.umbrella_fill),
                contentDescription = "Micrófono",
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (hasStarted) "Escuchando..." else "Procesando...",
            )
        }
    }
}
