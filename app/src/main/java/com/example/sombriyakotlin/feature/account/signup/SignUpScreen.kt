package com.example.sombriyakotlin.feature.account.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Vista de Registro
 */
@Composable
fun SignUpScreen(
    onNavigateBack: () -> Unit = {},
    onContinue: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }

    // Lienzo base
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF28BCEF)) // azul de fondo
    ) {
        // Marca superior
        Text(
            text = "Sombri-Ya",
            color = Color(0xFF001242),
            fontSize = 48.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 50.dp)
        )

        // -------- Tarjeta centrada vertical/horizontal --------
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 96.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(45.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
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
                            .size(56.dp)
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
                }
            }
        }

        // -------- Botón rojo anclado abajo --------
        Button(
            onClick = { onContinue() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF4645),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(1000.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .height(44.dp)
                .fillMaxWidth(0.74f) // ancho relativo (similar a 230px/393px)
        ) {
            Text("Seguir", fontSize = 18.sp)
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
    object : androidx.compose.ui.text.input.VisualTransformation {
        override fun filter(text: androidx.compose.ui.text.AnnotatedString):
                androidx.compose.ui.text.input.TransformedText {
            val mask = "•".repeat(text.text.length)
            return androidx.compose.ui.text.input.TransformedText(
                androidx.compose.ui.text.AnnotatedString(mask),
                androidx.compose.ui.text.input.OffsetMapping.Identity
            )
        }
    }

@Preview(showBackground = true)
@Composable
private fun PreviewSignUp() {
    SignUpScreen()
}
