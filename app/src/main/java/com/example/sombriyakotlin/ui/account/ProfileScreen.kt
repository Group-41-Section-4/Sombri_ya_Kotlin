package com.example.sombriyakotlin.ui.account

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.sombriyakotlin.R
import com.example.sombriyakotlin.ui.layout.TopBarMini
import com.example.sombriyakotlin.ui.navigation.Routes
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardProfile(navController: NavHostController, viewModel: ProfileScreenViewModel = hiltViewModel()) {
    Column {
        TopBarMini(navController, "Perfil")
        ContentCard(
            modifier = Modifier.fillMaxWidth(),
            navController = navController,
            viewModel = viewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentCard(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: ProfileScreenViewModel
) {
    var openDialogName by rememberSaveable { mutableStateOf(false) }
    var openDialogPassword by rememberSaveable { mutableStateOf(false) }
    var openDialogMail by rememberSaveable { mutableStateOf(false) }

    var currentName by rememberSaveable { mutableStateOf("Nombre") }
    var currentPassword by rememberSaveable { mutableStateOf("Nombre") }
    var currentMail by rememberSaveable { mutableStateOf("user@uniandes.edu.co") }

    val userDistance by viewModel.userDistance.collectAsState()
    val percentage = (userDistance / 5).toFloat().coerceIn(0f, 1f)

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        WaterLevelCircle(percentage = percentage, distance = userDistance)

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
                ) { openDialogName = true }
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
                    modifier = Modifier
                        .padding(end = 150.dp)
                        .align(Alignment.Start)
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
                ) { openDialogPassword = true }
        )

        // === EMAIL ===
        OutlinedTextField(
            value = currentMail,
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
        //
        Button(
            onClick = {
                /* TODO Hace falta borrar el usuario del estado*/
                navController.navigate(Routes.AUTH_GRAPH) {
                    popUpTo(Routes.MAIN_GRAPH) { inclusive = true }
                    launchSingleTop = true
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.red),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Cerrar Sesión")
        }
    }

    // === DIÁLOGOS ===
    if (openDialogName) {
        NameDialog(
            current = currentName,
            onDismiss = { openDialogName = false },
            onSave = { newName ->
                currentName = newName
                openDialogName = false
            }
        )
    } else if (openDialogPassword) {
        PasswordDialog(
            onDismiss = { openDialogPassword = false },
            onSave = { newPassword ->
                currentPassword = newPassword
                openDialogPassword = false
            }
        )
    } else if (openDialogMail) {
        MailDialog(
            current = currentMail,
            onDismiss = { openDialogMail = false },
            onSave = { newMail ->
                currentMail = newMail
                openDialogMail = false
            }
        )
    }
}


@Composable
fun WaterLevelCircle(
    percentage: Float,
    distance: Double,
    modifier: Modifier = Modifier
) {
    val primaryColor = colorResource(id = R.color.primary)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Distancia Seco",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Box(
            modifier = modifier
                .size(150.dp)
                .clip(CircleShape),
            contentAlignment = Alignment.Center // <- Alinea icono al centro
        ) {
            // Canvas con agua
            Canvas(modifier = Modifier.fillMaxSize()) {
                val waterLevel = size.height * (1 - percentage)
                val waveHeight = size.height * 0.05f

                // Fondo
                drawCircle(color = Color.LightGray.copy(alpha = 0.5f))

                // Agua
                val path = Path().apply {
                    moveTo(0f, waterLevel)
                    for (i in 0..size.width.toInt()) {
                        lineTo(
                            i.toFloat(),
                            waterLevel + sin(i * 0.02f) * waveHeight
                        )
                    }
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()
                }

                clipPath(Path().apply { addOval(androidx.compose.ui.geometry.Rect(Offset.Zero, size)) }) {
                    drawPath(path, color = primaryColor)
                }
            }

            // Icono de sombrilla en el centro
            Icon(
                painter = painterResource(id = R.drawable.umbrella_fill),
                contentDescription = "Sombrilla",
                tint = Color.White,
                modifier = Modifier.size(48.dp) // tamaño del icono
            )
        }

        Text(
            text = "${String.format("%.2f", distance)} km de 5km",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
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
                    label = { Text("Nuevo nombre") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(newName) }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun PasswordDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var newPassword by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        title = { Text("Cambiar Contraseña") },
        text = {
            Column(Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Nueva contraseña") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(newPassword) }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun MailDialog(
    current: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var newMail by rememberSaveable { mutableStateOf(current) }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        title = { Text("Cambiar Email") },
        text = {
            Column(Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = newMail,
                    onValueChange = { newMail = it },
                    label = { Text("Nuevo email") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(newMail) }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
