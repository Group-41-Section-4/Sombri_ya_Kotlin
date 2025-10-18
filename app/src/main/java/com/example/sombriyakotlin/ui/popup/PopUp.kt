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

