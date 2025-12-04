package com.example.sombriyakotlin.ui.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.sombriyakotlin.R
import com.example.sombriyakotlin.ui.layout.TopBarMini
import com.example.sombriyakotlin.ui.navigation.Routes

@Composable
fun AccountManagementScreen(
    navController: NavHostController,
    viewModel: ProfileScreenViewModel = hiltViewModel()
) {
    val profileState by viewModel.profileState.collectAsState()

    var currentName by rememberSaveable { mutableStateOf("") }
    var currentMail by rememberSaveable { mutableStateOf("") }

    var showNameDialog by remember { mutableStateOf(false) }
    var showEmailDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(profileState) {
        if (profileState is ProfileScreenViewModel.ProfileState.Success) {
            val user = (profileState as ProfileScreenViewModel.ProfileState.Success).user
            currentName = user.name
            currentMail = user.email
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FB))
    ) {
        TopBarMini(navController, "Administrar Cuenta")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AccountOptionItem(
                text = "Cambiar nombre",
                onClick = { showNameDialog = true }
            )

            AccountOptionItem(
                text = "Cambiar correo",
                onClick = { showEmailDialog = true }
            )

//            // Aquí podrías agregar "Cambiar contraseña" cuando tengas endpoint
//            AccountOptionItem(
//                text = "Cambiar contraseña",
//                onClick = { /* TODO: implementar cuando tengas endpoint */ }
//            )

//            AccountOptionItem(
//                text = "Editar foto",
//                onClick = { /* TODO: abrir picker de imagen y llamar updateUserImage */ }
//            )

            AccountOptionItem(
                text = "Eliminar cuenta",
                onClick = { showDeleteDialog = true },
                isDanger = true
            )
        }
    }

    if (showNameDialog) {
        SimpleTextDialog(
            title = "Cambiar nombre",
            initialText = currentName,
            label = "Nuevo nombre",
            onDismiss = { showNameDialog = false },
            onConfirm = { newName ->
                viewModel.updateUserName(newName)
                currentName = newName
                showNameDialog = false
            }
        )
    }

    if (showEmailDialog) {
        SimpleTextDialog(
            title = "Cambiar correo",
            initialText = currentMail,
            label = "Nuevo correo",
            onDismiss = { showEmailDialog = false },
            onConfirm = { newEmail ->
                viewModel.updateUserEmail(newEmail)
                currentMail = newEmail
                showEmailDialog = false
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar cuenta") },
            text = { Text("¿Estás seguro de que quieres eliminar tu cuenta? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteAccount(
                            onSuccess = {
                                navController.navigate(Routes.AUTH_GRAPH) {
                                    popUpTo(Routes.MAIN_GRAPH) { inclusive = true }
                                    launchSingleTop = true
                                }
                            },
                            onError = { /* podrías mostrar un Snackbar */ }
                        )
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEB5757),
                        contentColor = Color.White
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(24.dp)
        )
    }
}

@Composable
private fun AccountOptionItem(
    text: String,
    onClick: () -> Unit,
    isDanger: Boolean = false
) {
    val bg = Color.White
    val contentColor = if (isDanger) Color(0xFFEB5757) else Color(0xFF222222)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(bg, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            color = contentColor
        )
        Text(
            text = ">",
            color = contentColor
        )
    }
}

@Composable
private fun SimpleTextDialog(
    title: String,
    initialText: String,
    label: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var textValue by rememberSaveable { mutableStateOf(initialText) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            androidx.compose.material3.OutlinedTextField(
                value = textValue,
                onValueChange = { textValue = it },
                label = { Text(label) },
                singleLine = true
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(textValue) }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = MaterialTheme.colorScheme.surface
    )
}
