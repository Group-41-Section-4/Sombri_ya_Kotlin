package com.example.sombriyakotlin.feature.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sombriyakotlin.R
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavController




@OptIn(ExperimentalMaterial3Api::class)
//@Preview()
@Composable
fun CardProfile(navController: NavController) {

    Column {
        TopBar(navController)
        ContentCard(
            modifier = Modifier.fillMaxWidth()
        )
    }
    }
@OptIn(ExperimentalMaterial3Api::class)
//@Preview
@Composable()
fun TopBar(navController: NavController){

        TopAppBar(
            title = { Text("Cuenta",
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            ) },
             colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(R.color.primary),
                navigationIconContentColor = Color.Black
            ),

            navigationIcon = {
                IconButton(onClick = { navController.navigate("main") }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás"
                    )
                }
            }
        )
    }

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentCard(modifier: Modifier = Modifier) {
    var openDialogName by rememberSaveable { mutableStateOf(false) }
    var openDialogPassword by rememberSaveable { mutableStateOf(false) }
    var openDialogMail by rememberSaveable { mutableStateOf(false) }
    var openDialogDelete by rememberSaveable { mutableStateOf(false) }
    var openDialogDiseable by rememberSaveable { mutableStateOf(false) }


    var currentName by rememberSaveable { mutableStateOf("Nombre") }
    var currentPassword by rememberSaveable { mutableStateOf("Nombre") }
    var currentMail by rememberSaveable { mutableStateOf("user@uniandes.edu.co") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        IconButton(
            onClick = { /* acción foto */ },
            modifier = Modifier.size(160.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Foto de perfil",
                tint = Color.Black,
                modifier = Modifier.size(166.dp)
            )
        }

        Text("Cambiar foto de perfil")

        // === NOMBRE ===
        OutlinedTextField(
            value = currentName,
            onValueChange = {},
            readOnly = true,
            shape = RoundedCornerShape(24.dp),
            prefix = {
                Text(
                    "Nombre",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 160.dp)
                )
            },
            trailingIcon = {
                IconButton(onClick = { openDialogName = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = "Modificar información",
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                disabledContainerColor  = colorResource(R.color.secondary),
                focusedContainerColor   = colorResource(R.color.secondary),
                unfocusedContainerColor = colorResource(R.color.secondary),
                focusedTextColor        = colorResource(R.color.gray),
                unfocusedTextColor      = colorResource(R.color.gray),
                focusedIndicatorColor   = colorResource(R.color.primary),
                unfocusedIndicatorColor = colorResource(R.color.primary)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    openDialogName = true // <-- cambia estado aquí (sin llaves extra)
                }
        )

        // === CONTRASEÑA ===
        OutlinedTextField(
            value = currentPassword,
            onValueChange = {},
            readOnly = true,
            shape = RoundedCornerShape(24.dp),
            prefix = {
                Text(
                    "Contraseña",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 190.dp)
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { openDialogPassword = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = "Modificar información",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            ,
            colors = TextFieldDefaults.colors(
                disabledContainerColor  = colorResource(R.color.secondary),
                focusedContainerColor   = colorResource(R.color.secondary),
                unfocusedContainerColor = colorResource(R.color.secondary),
                focusedTextColor        = colorResource(R.color.gray),
                unfocusedTextColor      = colorResource(R.color.gray),
                focusedIndicatorColor   = colorResource(R.color.primary),
                unfocusedIndicatorColor = colorResource(R.color.primary)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    openDialogPassword = true // <-- cambia estado aquí (sin llaves extra)
                }
        )

        // === EMAIL ===
        OutlinedTextField(
            value =currentMail ,
            onValueChange = {},
            readOnly = true,
            shape = RoundedCornerShape(24.dp),
            prefix = {
                Text(
                    "Email",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 130.dp)
                )
            },
            trailingIcon = {
                IconButton(onClick = { openDialogMail = true }) {
                    Icon(
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = "Modificar información",
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                disabledContainerColor  = colorResource(R.color.secondary),
                focusedContainerColor   = colorResource(R.color.secondary),
                unfocusedContainerColor = colorResource(R.color.secondary),
                focusedTextColor        = colorResource(R.color.gray),
                unfocusedTextColor      = colorResource(R.color.gray),
                focusedIndicatorColor   = colorResource(R.color.primary),
                unfocusedIndicatorColor = colorResource(R.color.primary)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {openDialogDelete = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.red),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    openDialogMail = true
                }
        ) {
            Text("Borrar cuenta")
        }

        Button(
            onClick = { openDialogDiseable = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.gray),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Desactivar cuenta")
        }
    }

    // === DIBUJAR EL DIALOGO DESDE LA COMPOSICIÓN ===
    if (openDialogName) {
        NameDialog(
            current = currentName,
            onDismiss = { openDialogName = false },
            onSave = { newName ->
                currentName = newName
                openDialogName = false
            }
        )
    }
    else if(openDialogPassword){
        PasswordDialog(
            onDismiss = { openDialogPassword = false },
            onSave = { newPassword ->
                currentPassword=newPassword
                openDialogPassword = false
            }
        )

    }
    else if(openDialogMail){
        MailDialog(
            current = currentMail,
            onDismiss = { openDialogMail = false },
            onSave = { newMail ->
                currentMail= newMail
                openDialogMail = false
            }
        )

    }
    if (openDialogDelete) {
        DeleteDiseableAccountDialog(
            text = "Esta acción es permanente y no se puede deshacer. " +
                    "Se borrarán todos tus datos, historial y suscripciones. " +
                    "¿Estás seguro de que quieres continuar?",
            onDismiss = { openDialogDelete = false },
            onConfirm = {
                // TODO after delete account
                openDialogDelete = false
            }
        )
    }

    if (openDialogDiseable) {
        DeleteDiseableAccountDialog(
            tittle = "Desactivar Cuenta",
            text = "Al desactivar la cuenta, no recibirás más notificaciones y " +
                    "tus suscripciones se suspenderán al final del periodo de pago actual. " +
                    "¿Estás seguro?",
            onDismiss = { openDialogDiseable = false },
            onConfirm = {
                // TODO after delete account
                openDialogDiseable = false
            }
        )
    }
}


@Composable
fun NameDialog(
    current: String = "Nombre",
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var newName by rememberSaveable { mutableStateOf(current) }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        title = { Text("Cambiar Nombre") },
        text = {
            Column(Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Nuevo Nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(newName) },
                enabled = newName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.primary),
                    contentColor = Color.White
                ),
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
@Composable
fun PasswordDialog(
    onDismiss: () -> Unit,
    onSave: (newPassword: String) -> Unit
) {
    var currentPassword by rememberSaveable { mutableStateOf("") }
    var newPassword     by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    var showCurrent by rememberSaveable { mutableStateOf(false) }
    var showNew     by rememberSaveable { mutableStateOf(false) }
    var showConfirm by rememberSaveable { mutableStateOf(false) }

    val passwordsMatch = newPassword.isNotBlank() && newPassword == confirmPassword
    val enabledSave = currentPassword.isNotBlank() && passwordsMatch

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        title = { Text("Cambiar Contraseña") },
        text = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Contraseña actual
                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    singleLine = true,
                    label = { Text("Contraseña Actual") },
                    visualTransformation = if (showCurrent) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showCurrent = !showCurrent }) {
                            Icon(
                                imageVector = if (showCurrent) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (showCurrent) "Ocultar" else "Mostrar"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Nueva contraseña
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    singleLine = true,
                    label = { Text("Nueva Contraseña") },
                    visualTransformation = if (showNew) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showNew = !showNew }) {
                            Icon(
                                imageVector = if (showNew) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (showNew) "Ocultar" else "Mostrar"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Confirmar nueva contraseña
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    singleLine = true,
                    label = { Text("Confirmar Nueva Contraseña") },
                    isError = confirmPassword.isNotBlank() && !passwordsMatch,
                    supportingText = {
                        if (confirmPassword.isNotBlank() && !passwordsMatch) {
                            Text("Las contraseñas no coinciden")
                        }
                    },
                    visualTransformation = if (showConfirm) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showConfirm = !showConfirm }) {
                            Icon(
                                imageVector = if (showConfirm) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (showConfirm) "Ocultar" else "Mostrar"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(newPassword) },
                enabled = enabledSave,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.primary),
                    contentColor = Color.White
                )
            ) {
                Text("Guardar")
            }
        }
    )
}
@Composable
fun MailDialog(
    current: String = "Nombre",
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var newMail by rememberSaveable { mutableStateOf(current) }
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        title = { Text("Cambiar Correo") },
        text = {
            Column(Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = newMail,
                    onValueChange = { newMail = it },
                    label = { Text("Nuevo Correo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(newMail) },
                enabled = newMail.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.primary),
                    contentColor = Color.White
                ),
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
@Composable
fun DeleteDiseableAccountDialog(
    tittle: String = "Borrar Cuenta",
    text: String = "Esta acción es permanente y no se puede deshacer. ",
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        title = { Text(tittle) },
        text = {
            Text(
                text
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Regresar")
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.red),
                    contentColor = Color.White
                )
            ) {
                Text("Confirmar")
            }
        }
    )
}

@Composable
fun DisableAccountDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        title = { Text("Borrar Cuenta") },
        text = {
            Text(
                "Esta acción es permanente y no se puede deshacer. " +
                        "Se borrarán todos tus datos, historial y suscripciones. " +
                        "¿Estás seguro de que quieres continuar?"
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Regresar")
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.red),
                    contentColor = Color.White
                )
            ) {
                Text("Confirmar")
            }
        }
    )
}


//}


