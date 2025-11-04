package com.example.sombriyakotlin.ui.account.login

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sombriyakotlin.ui.account.login.LoginViewModel.LoginState
import com.example.sombriyakotlin.ui.popup.SomenthingWentWrongPopUp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch
import com.example.sombriyakotlin.R

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

    var errorMessage by remember { mutableStateOf("Algo salió mal :(") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }



    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> {
                isLoading=false
                // Navega solo cuando el registro es exitoso
                onContinue()
            }
            is LoginState.Error -> {
                // Muestra un Snackbar, Toast o un diálogo con el error
                val msg = (loginState as? LoginState.Error)?.message ?: "Algo salió mal :("
                errorMessage = msg
                showErrorDialog = true
                isLoading=false
            }
            is LoginState.Loading -> {
                isLoading=true

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

    )
    {

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
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        colors= ButtonColors(Color(0xFF001242),Color(0xFF001242),Color(0xFF001242),Color(0xFF001242)),
                        onClick = {viewModel.loginUser(email,pass)},
                        enabled = !isLoading)
                    {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = "Iniciar Sesión",
                                color = Color.White,
                                fontSize = 20.sp
                            )
                        }
                    }


                    GoogleButton(viewModel,isLoading)
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

            Text(
                text = "Ahorra tiempo y mantente seco en cualquier trayecto",
                color = Color(0xFF001242),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 36.dp)
            )
        }

        SomenthingWentWrongPopUp(
            show = showErrorDialog,
            message = errorMessage,
            onDismiss = { showErrorDialog = false }
        )
    }
}



@Composable
private fun GoogleButton(viewModel: LoginViewModel, isLoading: Boolean, ){
    var googleAuth by remember { mutableStateOf(false) }

    val baseContext = LocalContext.current
    val activity = remember(baseContext) { baseContext as? Activity } // puede ser null en previews

    val coroutineScope = rememberCoroutineScope()

    // GoogleSignInClient para fallback
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("751256331187-i160vbb6d96fo4bnnqhglrha2es9hla0.apps.googleusercontent.com")
        .requestEmail()
        .build()
    val gsc = GoogleSignIn.getClient(activity ?: baseContext, gso)
    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { res ->
        if (res.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(res.data)
            coroutineScope.launch {
                try {
                    val account = task.getResult(ApiException::class.java)
                    viewModel.googleLoginUser(account.idToken)

                    Log.d("LoginScreen", "Google SignIn success: ${account.idToken}")

                } catch (e: Exception) {
                    Log.w("LoginScreen", "Google SignIn fallback failed: ${e.localizedMessage}")
                }
            }
        } else {
            Log.w("LoginScreen", "GoogleSignIn canceled or failed, code=${res.resultCode}")
        }
    }


    LaunchedEffect(googleAuth) {
        if (!googleAuth) return@LaunchedEffect

        try {
            googleLauncher.launch(gsc.signInIntent)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("LoginScreen", "Couldn't retrieve user's credentials: ${e.localizedMessage}")
        }
    }

    Button(modifier = Modifier.wrapContentWidth(),
        colors= ButtonColors(
            Color(131314),
            Color(131314),
            Color(131314),
            Color(131314)),
        onClick = { googleAuth=true },
        enabled = !isLoading,
        border = BorderStroke(1.dp, Color(0xFF747775))

    )
    { //signInWithGoogleOption
        Row(
            modifier = Modifier.padding(0.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.google_logo2),
                contentDescription = "Google Logo",
                modifier = Modifier
                    .padding( 0.dp)
                    .size(20.dp)

            )
            Text("Acceder con Google",
                color = Color.Black,
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 8.dp)

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


