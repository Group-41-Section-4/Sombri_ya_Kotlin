package com.example.sombriyakotlin.ui.rent

import android.app.Activity
import android.nfc.NfcAdapter
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.sombriyakotlin.ui.layout.AppLayout
import com.example.sombriyakotlin.ui.navigation.Routes
import com.example.sombriyakotlin.feature.rent.NfcScanner // 🔹 Nuevo import (ver nota abajo)
import com.example.sombriyakotlin.ui.rent.scan.QrScannerScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardRent(navController: NavController) {
    val ctx = LocalContext.current
    val activity = remember(ctx) { ctx as Activity }

    val rentViewModel: RentViewModel = hiltViewModel()
    val rentState by rentViewModel.rentState.collectAsStateWithLifecycle()
    val hasActive by rentViewModel.hasActive.collectAsStateWithLifecycle()

    // 🔹 Efecto: si se completa la reserva, regresar al MAIN
    LaunchedEffect(rentState) {
        if (rentState is RentViewModel.RentState.Success) {
            navController.navigate(Routes.MAIN) {
                popUpTo(Routes.RENT) { inclusive = true }
            }
        }
    }

    // 🔹 Estado NFC
    var nfcEnabled by remember { mutableStateOf(false) }
    val nfcScanner = remember {
        NfcScanner(
            onTagDetected = { tagId ->
                Log.d("Rent", "Tag detectado: $tagId")
                rentViewModel.handleScanNfc(tagId)
            },
            onError = { errorMsg ->
                toast(activity, errorMsg)
            }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            Log.d("Rent", "Deteniendo NFC al salir de la pantalla…")
            nfcScanner.stop(activity)
        }
    }

    // 🔹 Pop-up: alquiler activo
    var showActivePopUp by remember { mutableStateOf(false) }
    LaunchedEffect(hasActive) { showActivePopUp = hasActive }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(Modifier.weight(1f).fillMaxSize()) {
            // 🔹 QR Scanner embebido
            QrScannerScreen(modifier = Modifier.matchParentSize())

            // 🔹 Botón flotante NFC
            BotonNFC(
                onClick = {
                    if (!isNfcSupported(activity)) {
                        toast(activity, "Este dispositivo no soporta NFC")
                        return@BotonNFC
                    }

                    if (!isNfcEnabled(activity)) {
                        toast(activity, "Activa NFC en los ajustes del sistema")
                        openNfcSettings(activity)
                        return@BotonNFC
                    }

                    if (!nfcEnabled) {
                        try {
                            nfcScanner.start(activity)
                            toast(activity, "NFC activado. Acerca la tarjeta…")
                            nfcEnabled = true
                        } catch (e: Exception) {
                            Log.e("Rent", "Error activando NFC", e)
                            toast(activity, "Error al activar NFC")
                        }
                    } else {
                        try {
                            nfcScanner.stop(activity)
                            toast(activity, "NFC desactivado")
                            nfcEnabled = false
                        } catch (e: Exception) {
                            Log.e("Rent", "Error desactivando NFC", e)
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 80.dp)
            )
        }
    }

    // 🔹 Pop-up: alquiler activo
    if (showActivePopUp) {
        AlquilerActivoPopUp(
            onIngresar = {
                rentViewModel.setReturnIntent()
                showActivePopUp = false
                nfcEnabled = false
                toast(activity, "Modo devolución activado. Acerca la tarjeta…")
            },
            onNo = {
                showActivePopUp = false
                navController.navigate(Routes.MAIN)
            },
            onDismiss = { showActivePopUp = false }
        )
    }
}

@Composable
fun AlquilerActivoPopUp(
    onIngresar: () -> Unit,
    onNo: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tienes un alquiler activo") },
        text = { Text("¿Quieres ingresar a dejar tu sombrilla ahora?") },
        confirmButton = {
            TextButton(onClick = onIngresar) {
                Text("Ingresar a dejar sombrilla")
            }
        },
        dismissButton = {
            TextButton(onClick = onNo) {
                Text("No")
            }
        }
    )
}

@Composable
fun BotonNFC(onClick: () -> Unit, modifier: Modifier = Modifier) {
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
private fun isNfcSupported(activity: Activity): Boolean =
    NfcAdapter.getDefaultAdapter(activity) != null

private fun isNfcEnabled(activity: Activity): Boolean =
    NfcAdapter.getDefaultAdapter(activity)?.isEnabled == true

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
fun TopBar(navController: NavController) {
    TopAppBar(
        title = { Text("") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(R.color.BlueInterface),
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
