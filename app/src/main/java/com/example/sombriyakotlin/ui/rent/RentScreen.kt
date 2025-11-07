package com.example.sombriyakotlin.ui.rent

import android.app.Activity
import android.nfc.NfcAdapter
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.sombriyakotlin.R
import com.example.sombriyakotlin.ui.layout.AppLayout
import com.example.sombriyakotlin.ui.navigation.Routes
import com.example.sombriyakotlin.feature.rent.Scan.NfcScanner // 🔹 Nuevo import (ver nota abajo)
import com.example.sombriyakotlin.ui.rent.scan.QrScannerScreen
import com.example.sombriyakotlin.ui.utils.isOnline


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardRent(navController: NavController) {
    val ctx = LocalContext.current
    val activity = remember(ctx) { ctx as Activity }

    val rentViewModel: RentViewModel = hiltViewModel()
    val rentState by rentViewModel.rentState.collectAsStateWithLifecycle()
    // val hasActive by rentViewModel.hasActive.collectAsStateWithLifecycle()

    // Estados de los pop-ups
    var showReservaPopup by remember { mutableStateOf(false) }
    var showDevolucionPopup by remember { mutableStateOf(false) }
    var showActivePopUp by remember { mutableStateOf(false) }
    var suppressActivePopup by remember { mutableStateOf(false) }
    var showNoInternetPopup by remember { mutableStateOf(false) }
    var navigateToMain by remember { mutableStateOf(false) }
    val qrViewModel: QrViewModel = hiltViewModel()

    var showErrorPopup by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }



    // Estado NFC
    var nfcEnabled by remember { mutableStateOf(false) }
    val nfcScanner = remember {
        NfcScanner(
            onTagDetected = { tagId ->
                Log.d("Rent", "Tag detectado: $tagId")
                rentViewModel.handleScan(tagId)
            },
            onError = { errorMsg -> toast(activity, errorMsg) }
        )
    }

    //   Verificar conexión al entrar
    LaunchedEffect(Unit) {
        if (!isOnline(activity)) {
            showNoInternetPopup = true
            Log.d("Rent", "Sin conexión: se muestra pop-up y se detiene flujo.")
            return@LaunchedEffect // 👈 Detiene el resto del código inicial
        }

        //  Solo si hay conexión, activar NFC
        if (isNfcSupported(activity)) {
            if (isNfcEnabled(activity)) {
                try {
                    nfcScanner.start(activity)
                    nfcEnabled = true
                    toast(activity, "NFC activado automáticamente. Acerca la tarjeta…")
                } catch (e: Exception) {
                    Log.e("Rent", "Error activando NFC", e)
                    toast(activity, "Error al activar NFC automáticamente")
                }
            } else {
                toast(activity, "Activa NFC en los ajustes del sistema")
                openNfcSettings(activity)
            }
        } else {
            toast(activity, "Este dispositivo no soporta NFC")
        }
    }

    // Detener NFC al salir
    DisposableEffect(Unit) {
        onDispose {
            Log.d("Rent", "Deteniendo NFC al salir de la pantalla…")
            nfcScanner.stop(activity)
            suppressActivePopup = false
        }
    }

    // Manejar cambios en el estado del alquiler
    LaunchedEffect(rentState) {
        val currentState = rentState // 🔹 Creamos una variable local para permitir el smart cast

        when (currentState) {
            is RentViewModel.RentState.Success -> {
                try { nfcScanner.stop(activity) } catch (_: Exception) {}
                showActivePopUp = false
                suppressActivePopup = true

                val endedAt = currentState.rental.endedAt
                if (!endedAt.isNullOrBlank()) {
                    showDevolucionPopup = true
                } else {
                    showReservaPopup = true
                }
            }

            is RentViewModel.RentState.Error -> {
                errorMessage = currentState.message
                showErrorPopup = true
            }

            else -> Unit
        }
    }



    //Navegación hacia la pantalla principal
    if (navigateToMain) {
        LaunchedEffect(Unit) {
            navController.navigate(Routes.MAIN) {
                popUpTo(Routes.RENT) { inclusive = true }
            }
            navigateToMain = false
        }
    }

    //Hay renta solo si hay red
    LaunchedEffect(showReservaPopup, showDevolucionPopup, suppressActivePopup) {
        if (!isOnline(activity)) return@LaunchedEffect // evita errores sin conexión

        val active = rentViewModel.checkActiveRental()
        showActivePopUp = active && !showReservaPopup && !showDevolucionPopup && !suppressActivePopup
    }


    // UI principal
    Column(modifier = Modifier.fillMaxSize()) {
        Box(Modifier.weight(1f).fillMaxSize()) {
            QrScannerScreen(modifier = Modifier.matchParentSize())
        }
    }

    // Pop-ups
    if (showNoInternetPopup) {
        PopUpNoInternet(
            onDismiss = { showNoInternetPopup = false },
            navController = navController
        )
    }
    if (showDevolucionPopup) {
        PopUpDevolucionExitosa(onDismiss = {
            showDevolucionPopup = false
            nfcEnabled = false
            rentViewModel.reset()
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
    if (showActivePopUp && !showReservaPopup && !showDevolucionPopup) {
        AlquilerActivoPopUp(
            onIngresar = {
                rentViewModel.setReturnIntent()
                showActivePopUp = false
                nfcEnabled = false
            },
            onNo = {
                showActivePopUp = false
                navController.navigate(Routes.MAIN)
            },
            onDismiss = { showActivePopUp = false }
        )
    }
    if (showErrorPopup) {
        PopUpError(message = errorMessage) {
            showErrorPopup = false
            rentViewModel.reset()

            // Reactiva NFC al cerrar el pop-up
            try {
                nfcScanner.start(activity)
                nfcEnabled = true
                toast(activity, "NFC reactivado. Acerca la tarjeta…")
            } catch (e: Exception) {
                Log.e("Rent", "Error reactivando NFC", e)
                toast(activity, "No se pudo reactivar NFC automáticamente")
            }
        }
    }

    // bloquear el escaneo si hay pop-ups abiertos
    LaunchedEffect(showReservaPopup, showDevolucionPopup, showActivePopUp, showNoInternetPopup) {
        val anyPopupVisible = showReservaPopup || showDevolucionPopup || showActivePopUp || showNoInternetPopup
        qrViewModel.enableScanning(!anyPopupVisible)
        Log.d("QR_SCAN", "Escaneo habilitado: ${!anyPopupVisible}")
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
@Composable
fun PopUpNoInternet(
    onDismiss: () -> Unit,
    navController: NavController
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Sin conexión a Internet") },
        text = { Text("Por favor verifica tu conexión antes de continuar.") },
        confirmButton = {
            TextButton(onClick = {
                onDismiss()
                // 👇 Al cerrar, navega al main
                navController.navigate(Routes.MAIN) {
                    popUpTo(Routes.RENT) { inclusive = true }
                }
            }) {
                Text("OK")
            }
        }
    )
}

@Composable
fun PopUpError(message: String, onDismiss: () -> Unit) {
    // 🔹 Función para limpiar mensajes técnicos
    fun cleanMessage(raw: String): String {
        return when {
            raw.contains("timeout", true) -> "La conexión tardó demasiado. Revisa tu Internet e inténtalo otra vez."
            else -> "Upsss No se pudo realizar la renta. Intenta nuevamente."
        }
    }

    val prettyMessage = cleanMessage(message)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Error en la renta") },
        text = { Text(prettyMessage) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Aceptar")
            }
        }
    )
}





//@RequiresApi(Build.VERSION_CODES.M)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainRenta(navController: NavController,navHostController: NavHostController) {
    AppLayout(navController = navController,navHostController) {
        CardRent(navController)
    }
}
