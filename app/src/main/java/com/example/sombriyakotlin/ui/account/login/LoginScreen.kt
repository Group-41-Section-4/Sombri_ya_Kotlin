package com.example.sombriyakotlin.ui.account.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sombriyakotlin.ui.account.login.LoginViewModel.LoginState
//import com.example.sombriyakotlin.ui.account.signInWithGoogleOption

@Composable
fun LoginScreen(
    onNavigateToSignUp: () -> Unit = {},
    onContinue: () -> Unit = {},
    viewModel: LoginViewModel = hiltViewModel()
) {

    // Estados locales de los inputs (sólo para que se vea el hint/edición)
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    val loginState by viewModel.loginState.collectAsState()

    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> {
                // Navega solo cuando el registro es exitoso
                onContinue()
            }
            is LoginState.Error -> {
                // Muestra un Snackbar, Toast o un diálogo con el error
                // scaffoldState.snackbarHostState.showSnackbar((signUpState as SignUpState.Error).message)
            }
            else -> { /* No hacer nada en Idle o Loading */ }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF90E0EF)), // background: #28BCEF
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly

    ) {

        // --- Título "Sombri-Ya" (arriba)
        Text(
            text = "Sombri-Ya",
            color = Color(0xFF001242),
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(top = 50.dp)
//                .align(Alignment.TopCenter)
//                .offset(y = 105.dp)   // left/top exactos de Figma
        )

        // --- Tarjeta principal (Rectangle 3)
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .wrapContentHeight()

//                .align(Alignment.Center)
                .shadow(8.dp, RoundedCornerShape(25.dp), clip = false)
                .clip(RoundedCornerShape(25.dp))
                .background(Color(0xFFFFFDFD)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp, horizontal = 25.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
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
                        .size(width = 57.dp, height = 57.dp)
                        .background(Color.White)
                        .padding(top = 7.dp),
                    contentAlignment = Alignment.Center
                )
                {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "personIcon",
                        tint = Color(0xFF001242),
                        modifier = Modifier.size(50.dp)
                    )
                }

                // Campo Email → (l:55-22=33, t:332-205=127) 284×40
                LoginInput(
                    label = "Correo electrónico",
                    value = email,
                    onValueChange = { email = it },
                    hint = "user@ejemplo.com",
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo Contraseña → (l:55-22=33, t:397-205=192) 284×40
                LoginInput(
                    label = "Contraseña",
                    value = pass,
                    onValueChange = { pass = it },
                    hint = "contraseña",
                    modifier = Modifier.fillMaxWidth(),
                    )

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                )
                {
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
                            .padding(bottom = 16.dp)
                    )
                }

                // Botón: Iniciar sesión → (l:82-22=60, t:579-205=374) 230×36
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(modifier = Modifier.fillMaxWidth(),colors= ButtonColors(Color(0xFF001242),Color(0xFF001242),Color(0xFF001242),Color(0xFF001242)), onClick = {viewModel.loginUser("5e1a88f1-55c5-44d0-87bb-44919f9f4202")})
                    {
                        Text(
                            text = "Iniciar Sesión",
                            color = Color.White,
                            fontSize = 20.sp,
                        )
                    }


                    Button(modifier = Modifier.fillMaxWidth(), colors= ButtonColors(Color(0xFF001242),Color(0xFF001242),Color(0xFF001242),Color(0xFF001242)), onClick = { }) { //signInWithGoogleOption
                        Text(
                            text = "Iniciar Sesión con Google",
                            color = Color.White,
                            fontSize = 20.sp,
                        )
                    }
                }
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp)
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            // Texto "Sombri-Ya" inferior → (l:140, t:699)
            Text(
                text = "Sombri-Ya",
                color = Color(0xFF001242),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
//            modifier = Modifier.offset(x = 140.dp, y = 699.dp)
            )

            // Slogan → (l:65, t:754) w:263
            Text(
                text = "Ahorra tiempo y mantente seco en cualquier trayecto",
                color = Color(0xFF001242),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 36.dp)
            )
        }
    }
}

/**
 * Caja de input gris (E6E6E6) con borde D9D9D9 y radio 8dp,
 * tal como Figma. Es un TextField minimal con estilos custom.
 */
@Composable
private fun LoginInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier)
    {
        Column(
            modifier = modifier.padding(top = 10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = label,
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            val shape = RoundedCornerShape(8.dp)
            // Caja visual
            Box(
                modifier = modifier
                    .shadow(0.dp, shape) // sin sombra externa
                    .clip(shape)
                    .background(Color(0xFFE6E6E6))
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = shape
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.CenterStart
            )
            {
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
}

@Preview(showBackground = true, backgroundColor = 0xFF28BCEF)
@Composable
private fun PreviewLogin() {
    LoginScreen()
}
