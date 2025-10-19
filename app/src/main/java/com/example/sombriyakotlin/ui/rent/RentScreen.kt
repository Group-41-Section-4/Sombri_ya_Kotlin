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
import com.example.sombriyakotlin.feature.rent.Scan.NfcScanner // 🔹 Nuevo import (ver nota abajo)
import com.example.sombriyakotlin.ui.rent.scan.QrScannerScreen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardRent(navController: NavController) {
    val ctx = LocalContext.current
    val activity = remember(ctx) { ctx as Activity }

    val rentViewModel: RentViewModel = hiltViewModel()
    val rentState by rentViewModel.rentState.collectAsStateWithLifecycle()
    val hasActive by rentViewModel.hasActive.collectAsStateWithLifecycle()
    var showReservaPopup by remember { mutableStateOf(false) }
    var navigateToMain by remember { mutableStateOf(false) }
    var showActivePopUp by remember { mutableStateOf(false) }
    var showDevolucionPopup by remember { mutableStateOf(false) }   // 🆕
    var suppressActivePopup by remember { mutableStateOf(false) }   // 🆕 evita el flash




    // 🔹 Estado NFC
    var nfcEnabled by remember { mutableStateOf(false) }
    val nfcScanner = remember {
        NfcScanner(
            onTagDetected = { tagId ->
                Log.d("Rent", "Tag detectado: $tagId")
                rentViewModel.handleScan(tagId)

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
            suppressActivePopup = false
        }
    }
    LaunchedEffect(rentState) {
        if (rentState is RentViewModel.RentState.Success) {
            try { nfcScanner.stop(activity) } catch (_: Exception) {}
            showActivePopUp = false
            suppressActivePopup = true
            val endedAt = (rentState as RentViewModel.RentState.Success).rental.endedAt
            if (!endedAt.isNullOrBlank()) {
                // Devolución exitosa
                showDevolucionPopup = true
            } else {
                // Reserva exitosa
                showReservaPopup = true
            }
        }
    }
    if (navigateToMain) {
        // Esta navegación se hace fuera del árbol composable
        LaunchedEffect(Unit) {
            navController.navigate(Routes.MAIN) {
                popUpTo(Routes.RENT) { inclusive = true }
            }
            navigateToMain = false
        }
    }

    LaunchedEffect(hasActive, showReservaPopup, showDevolucionPopup, suppressActivePopup) {
        showActivePopUp = hasActive && !showReservaPopup && !showDevolucionPopup && !suppressActivePopup
    }
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

    if (showDevolucionPopup) {
        PopUpDevolucionExitosa(onDismiss = {
            showDevolucionPopup = false
            nfcEnabled = false
            rentViewModel.reset()
            // normalmente navegas al main después de devolver
            navigateToMain = true
        })
    }


    if (showReservaPopup) {
        PopUpReservaCreated(onDismiss = {
            showReservaPopup = false
            nfcEnabled = false
            rentViewModel.reset()
            navigateToMain = true
        })
    }

    // 🔹 Pop-up: alquiler activo
    if (showActivePopUp && !showReservaPopup && !showDevolucionPopup) {
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
@Composable
fun PopUpReservaCreated(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Reserva realizada") },
        text = { Text("Reserva realizada con éxito") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}
@Composable
fun PopUpDevolucionExitosa(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Devolución realizada") },
        text = { Text("Has devuelto la sombrilla con éxito.") },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
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
