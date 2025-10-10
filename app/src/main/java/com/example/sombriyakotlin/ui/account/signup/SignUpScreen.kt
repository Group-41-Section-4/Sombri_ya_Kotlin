package com.example.sombriyakotlin.ui.account.signup

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sombriyakotlin.R
import com.example.sombriyakotlin.ui.account.signup.SingUpViewModel.SignUpState

/**
 * Vista de Registro
 */
//@SuppressLint("ResourceAsColor")
@Composable
fun SignUpScreen(
    onNavigateBack: () -> Unit = {},
    onContinue: () -> Unit = {},
    viewModel: SingUpViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }

    val signUpState by viewModel.signUpState.collectAsState()

    LaunchedEffect(signUpState) {
        when (signUpState) {
            is SignUpState.Success -> {
                // Navega solo cuando el registro es exitoso
                onContinue()
            }
            is SignUpState.Error -> {
                // Muestra un Snackbar, Toast o un diálogo con el error
                //scaffoldState.snackbarHostState.showSnackbar((signUpState as SignUpState.Error).message)
            }
            else -> { /* No hacer nada en Idle o Loading */ }
        }
    }

    // Lienzo base
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.BlueInterface)), // azul de fondo
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Marca superior
        Text(
            text = "Sombri-Ya",
            color = Color(0xFF001242),
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(top = 50.dp)
        )

        // -------- Tarjeta centrada vertical/horizontal --------
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,

        ) {
            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(45.dp),
//                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDFD))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Título gordito
                    Text(
                        text = "Regístrate",
                        color = Color(0xFF263238),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    // Texto para volver al login
                    Text(
                        text = "¿Ya tienes cuenta? Inicia sesión",
                        color = Color(0xFF001242),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 4.dp)
                            .clickable { onNavigateBack() }
                    )

                    // Icono en cajita con sombra
                    Box(
                        modifier = Modifier
                            .padding(top = 12.dp, bottom = 8.dp)
//                            .size(56.dp)
                            .shadow(6.dp, RoundedCornerShape(8.dp), clip = false)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                            tint = Color(0xFF001242)
                        )
                    }

                    // Campos
                    LabeledInput(
                        label = "Nombre",
                        value = name,
                        onValueChange = { name = it },
                        hint = "Tu nombre",
                        modifier = Modifier.fillMaxWidth()
                    )
                    LabeledInput(
                        label = "Correo electrónico",
                        value = email,
                        onValueChange = { email = it },
                        hint = "usuario@example.com",
                        modifier = Modifier.fillMaxWidth()
                    )
                    LabeledInput(
                        label = "Contraseña",
                        value = pass,
                        onValueChange = { pass = it },
                        hint = "contraseña",
                        modifier = Modifier.fillMaxWidth(),
                        isPassword = true
                    )
                    LabeledInput(
                        label = "Confirmar contraseña",
                        value = confirm,
                        onValueChange = { confirm = it },
                        hint = "Contraseña",
                        modifier = Modifier.fillMaxWidth(),
                        isPassword = true
                    )

                    // -------- Botón rojo anclado abajo --------
                    Button(
                        onClick = {
                            viewModel.registerUser(name, email, pass)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF4645),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(1000.dp),
                        enabled = signUpState != SignUpState.Loading,
                        modifier = Modifier.padding(top = 15.dp)
//                .height(44.dp)
//                .fillMaxWidth(0.74f) // ancho relativo (similar a 230px/393px)
                    ) {
                        if (signUpState == SignUpState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.Gray
                            )
                        } else {
                            Text("Seguir", fontSize = 18.sp)
                        }
                    }
                }

            }
        }

    }
}

/** Etiqueta + caja gris con borde como en el login (responsive). */
@Composable
private fun LabeledInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false
) {
    Column(
        modifier = modifier.padding(top = 10.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = label,
            color = Color(0xFF263238),
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        val shape = RoundedCornerShape(8.dp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(shape)
                .background(Color(0xFFE6E6E6))
                .border(1.dp, Color(0xFFD9D9D9), shape)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (value.isEmpty()) {
                Text(
                    text = hint,
                    color = Color(0xFF1E1E1E).copy(alpha = 0.45f),
                    fontSize = 16.sp
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    color = Color(0xFF1E1E1E)
                ),
                visualTransformation = if (isPassword)
                    PasswordVisualTransformationDot()
                else
                    VisualTransformation.None,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/** VisualTransformation básica con puntos para contraseñas (sin lógica). */
@Composable
private fun PasswordVisualTransformationDot() =
    object : VisualTransformation {
        override fun filter(text: AnnotatedString):
                TransformedText {
            val mask = "•".repeat(text.text.length)
            return TransformedText(
                AnnotatedString(mask),
                OffsetMapping.Identity
            )
        }
    }
