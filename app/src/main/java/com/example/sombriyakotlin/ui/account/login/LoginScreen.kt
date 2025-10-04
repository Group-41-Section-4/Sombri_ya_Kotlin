package com.example.sombriyakotlin.ui.account.login

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Pantalla Login (sólo vista)
 */
@Composable
fun LoginScreen(
    onNavigateToSignUp: () -> Unit = {},
    onContinue: () -> Unit = {}
) {

    // Estados locales de los inputs (sólo para que se vea el hint/edición)
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    // Lienzo base de 393x852 — los offsets están calc. para esa maqueta
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF90E0EF)) // background: #28BCEF
    ) {

        // --- Título "Sombri-Ya" (arriba)
        Text(
            text = "Sombri-Ya",
            color = Color(0xFF001242),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 105.dp)   // left/top exactos de Figma
        )

        // --- Tarjeta principal (Rectangle 3)
        Box(
            modifier = Modifier
                .size(width = 349.dp, height = 445.dp)
                .offset(x = 22.dp, y = 205.dp)
                .shadow(8.dp, RoundedCornerShape(25.dp), clip = false)
                .clip(RoundedCornerShape(25.dp))
                .background(Color(0xFFFFFDFD)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
            // Dentro de la tarjeta: offsets RELATIVOS al card
            // (restando 22 y 205 de los valores globales)

                // Título: "Inicia Sesión" (top 218 → 218-205 = 13)
                Text(
                    text = "Inicia Sesión",
                    color = Color.Black,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                )

                // Caja del ícono (person) → (l:170-22=148, t:285-205=80) 53x57
                Box(
                    modifier = Modifier
                        .size(width = 53.dp, height = 57.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "person",
                        tint = Color(0xFF001242),
                        modifier = Modifier.size(50.dp)
                    )
                }

                // Campo Email → (l:55-22=33, t:332-205=127) 284×40
                LoginInput(
                    value = email,
                    onValueChange = { email = it },
                    hint = "xxx@ejemplo.com",
                    modifier = Modifier
                        .height(80.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 16.dp)

                )

                // Campo Contraseña → (l:55-22=33, t:397-205=192) 284×40
                LoginInput(
                    value = pass,
                    onValueChange = { pass = it },
                    hint = "contraseña",
                    modifier = Modifier
                        .height(80.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 16.dp)
                )

                // ¿Olvidaste tu contraseña? → (l:118-22=96, t:511-205=306)
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    color = Color(0xFF001242),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    // modifier = Modifier.offset(x = 96.dp, y = 306.dp)
                )

                // ¿No tienes una cuenta? Regístrate → (l:84-22=62, t:545-205=340)
                Text(
                    text = "¿No tienes una cuenta? Regístrate",
                    color = Color(0xFF001242),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clickable { onNavigateToSignUp() }
                )

                // Botón: Iniciar sesión → (l:82-22=60, t:579-205=374) 230×36
                Box(
                    modifier = Modifier
                        .size(width = 230.dp, height = 36.dp)
                        .shadow(6.dp, RoundedCornerShape(25.dp), clip = false)
                        .clip(RoundedCornerShape(25.dp))
                        .background(Color(0xFF001242))
                        .clickable { onContinue() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Seguir",
                        color = Color.White,
                        fontSize = 22.sp,
                        letterSpacing = (-0.26f).sp
                    )
                }
            }
        }

        // Texto "Sombri-Ya" inferior → (l:140, t:699)
        Text(
            text = "Sombri-Ya",
            color = Color(0xFFFFFAFA),
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.offset(x = 140.dp, y = 699.dp)
        )

        // Slogan → (l:65, t:754) w:263
        Text(
            text = "Ahorra tiempo y mantente seco en cualquier trayecto",
            color = Color(0xFFFFFAFA),
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .offset(x = 65.dp, y = 754.dp)
                .width(263.dp)
        )
    }
}

/**
 * Caja de input gris (E6E6E6) con borde D9D9D9 y radio 8dp,
 * tal como Figma. Es un TextField minimal con estilos custom.
 */
@Composable
private fun LoginInput(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(8.dp)

    // Caja visual
    Box(
        modifier = modifier
            .shadow(0.dp, shape) // sin sombra externa
            .clip(shape)
            .background(Color(0xFFE6E6E6))
            .border(
                width = 1.dp,
                color = Color(0xFFD9D9D9),
                shape = shape
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        // Simulación del placeholder
        if (value.isEmpty()) {
            Text(
                text = hint,
                color = Color(0xFF1E1E1E).copy(alpha = 0.6f),
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
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF28BCEF)
@Composable
private fun PreviewLogin() {
    LoginScreen()
}
