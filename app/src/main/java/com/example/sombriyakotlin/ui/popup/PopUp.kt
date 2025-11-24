package com.example.sombriyakotlin.ui.popup

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable


@Composable
fun SomenthingWentWrongPopUp(
    show: Boolean,
    message: String = "Algo salió mal :(",
    onDismiss: () -> Unit
) {
    if (!show) return

    AlertDialog(
        onDismissRequest = { onDismiss() }, // permite tocar fuera para cerrar si quieres
        title = { Text("Información") },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Ok")
            }
        }
    )
}

@Composable
fun ConsentDialog(onAllow: () -> Unit, onDeny: () -> Unit) {
    AlertDialog(
        onDismissRequest = { /* evitar dismiss si quieres forzar respuesta; o llamar onDeny */ },
        title = { Text("Enviar ubicación") },
        text = {
            Text("¿Deseas enviar tu ubicación al abrir la app? Se usará para mejorar la aplicación y se mantendrá privada.")
        },
        confirmButton = {
            TextButton(onClick = onAllow) { Text("Permitir") }
        },
        dismissButton = {
            TextButton(onClick = onDeny) { Text("No permitir") }
        }
    )
}

