package com.example.sombriyakotlin.ui.rent

import android.app.Activity
import android.nfc.NfcAdapter
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.sombriyakotlin.R
import com.example.sombriyakotlin.feature.rent.NfcScanStrategy
import com.example.sombriyakotlin.ui.layout.AppLayout
import com.example.sombriyakotlin.ui.navigation.Routes
import com.example.sombriyakotlin.ui.rent.Scan.QrScannerScreen
import com.example.sombriyakotlin.ui.rent.Scan.ScanStrategy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardRent(navController: NavController) {
    val ctx = LocalContext.current
    val activity = remember(ctx) { ctx as Activity }

    val rentViewModel: RentViewModel = hiltViewModel()
    val rentState by rentViewModel.rentState.collectAsStateWithLifecycle()

    LaunchedEffect(rentState) {
        if (rentState is RentViewModel.RentState.Success) {
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.RENT) { inclusive = true }
            }
        }
    }

    var strategy: ScanStrategy? by remember { mutableStateOf(null) }

    // Instancia NFC (persistente)
    val nfc by remember {
        mutableStateOf(
            NfcScanStrategy { stationId ->
                Log.d("Rent", "➡️ onTagDetected($stationId) llamado")
                rentViewModel.createReservation(stationId)
            }
        )
    }

    // Log ciclo de vida y stop seguro
    LaunchedEffect(Unit) { Log.d("Rent", "🧭 CardRent montado") }
    DisposableEffect(Unit) {
        onDispose {
            Log.d("Rent", "🧹 CardRent dispose -> stop NFC si estaba activo")
            try { strategy?.stop(activity) } catch (_: Exception) {}
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(Modifier.weight(1f).fillMaxSize()) {
            // Contenido principal (tu QR)
            ContentCard(Modifier.matchParentSize())

            // Único FAB
            BotonNFC(
                onClick = {
                    Log.d("Rent", "🖱️ FAB clicado. strategy=$strategy")
                    if (!isNfcSupported(activity)) {
                        toast(activity, "Este dispositivo no soporta NFC")
                        Log.d("Rent", "❌ NFC no soportado")
                        return@BotonNFC
                    }
                    if (!isNfcEnabled(activity)) {
                        Log.d("Rent", "⚠️ NFC apagado -> abre ajustes")
                        openNfcSettings(activity)
                        toast(activity, "Activa NFC y vuelve a intentarlo")
                        return@BotonNFC
                    }

                    if (strategy == null) {
                        Log.d("Rent", "🔛 Activando NFC ReaderMode…")
                        try {
                            nfc.start(activity)
                            strategy = nfc
                            Log.d("Rent", "✅ NFC Activado")
                            toast(activity, "NFC activado. Acerca la tarjeta…")
                        } catch (e: Exception) {
                            Log.e("Rent", "❌ Error activando NFC", e)
                            toast(activity, "Error activando NFC")
                        }
                    } else {
                        Log.d("Rent", "🛑 Desactivando NFC ReaderMode…")
                        try {
                            strategy?.stop(activity)
                            strategy = null
                            Log.d("Rent", "✅ NFC Desactivado")
                            toast(activity, "NFC desactivado")
                        } catch (e: Exception) {
                            Log.e("Rent", "❌ Error desactivando NFC", e)
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 80.dp)
            )
        }
    }
}

@Composable
fun ContentCard(modifier: Modifier = Modifier) {
    QrScannerScreen(modifier = modifier)
}

@Composable
fun BotonNFC(onClick: () -> Unit, modifier: Modifier = Modifier){
    ExtendedFloatingActionButton(
        onClick = onClick,
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.vector),
                contentDescription = "NFC",
                modifier = Modifier.size(24.dp)
            )
        },
        contentColor = colorResource(R.color.botonNfc),
        containerColor = colorResource(R.color.white),
        text = { Text("Activar NFC") },
        modifier = modifier
            .width(199.dp)
            .height(40.dp)
            .padding(horizontal = 25.dp, vertical = 5.dp)
    )
}

// Utils
private fun isNfcSupported(activity: Activity): Boolean {
    val adapter = NfcAdapter.getDefaultAdapter(activity)
    return adapter != null
}

private fun isNfcEnabled(activity: Activity): Boolean {
    val adapter = NfcAdapter.getDefaultAdapter(activity)
    return adapter?.isEnabled == true
}

private fun openNfcSettings(activity: Activity) {
    try {
        activity.startActivity(android.content.Intent(android.provider.Settings.ACTION_NFC_SETTINGS))
    } catch (_: Exception) {
        activity.startActivity(android.content.Intent(android.provider.Settings.ACTION_SETTINGS))
    }
}

private fun toast(activity: Activity, msg: String) {
    android.widget.Toast.makeText(activity, msg, android.widget.Toast.LENGTH_SHORT).show()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController : NavController){
    TopAppBar(
        title = { Text("") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(R.color.primary),
            navigationIconContentColor = Color.Black
        ),
        navigationIcon = {
            IconButton(onClick = { navController.navigate("notifications") }) {
                Icon(imageVector = Icons.Outlined.Notifications, contentDescription = "notificaciones")
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate("profile") }) {
                Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = "Perfil")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainRenta(navController: NavController) {
    AppLayout(navController = navController) {
        CardRent(navController)
    }
}
