package com.example.sombriyakotlin.ui.account.signup

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
import androidx.compose.ui.focus.onFocusChanged
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
import com.example.sombriyakotlin.ui.popup.SomenthingWentWrongPopUp

/**
 * Vista de Registro
 */
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

    var errorMessage by remember { mutableStateOf("Algo salió mal :(") }
    var showErrorDialog by remember { mutableStateOf(false) }
    LaunchedEffect(signUpState) {
        when (signUpState) {
            is SignUpState.Success -> {
                // Navega solo cuando el registro es exitoso
                onContinue()
            }
            is SignUpState.Error -> {
                // Muestra un Snackbar, Toast o un diálogo con el error
                val msg = (signUpState as? SignUpState.Error)?.message ?: "Algo salió mal :("
                errorMessage = msg
                showErrorDialog = true
            }
            else -> { /* No hacer nada en Idle o Loading */ }
        }
    }

    // errores por campo (null = sin error)
    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passError by remember { mutableStateOf<String?>(null) }
    var confirmError by remember { mutableStateOf<String?>(null) }

    // touched flags
    var nameTouched by remember { mutableStateOf(false) }
    var emailTouched by remember { mutableStateOf(false) }
    var passTouched by remember { mutableStateOf(false) }
    var confirmTouched by remember { mutableStateOf(false) }

    // submitAttempted para forzar mostrar errores al intentar enviar
    var submitAttempted by remember { mutableStateOf(false) }

    // validadores individuales
    fun validateName(): String? =
        if (name.isBlank() && nameTouched) "El nombre no puede estar vacío"
        else
        when{
            (name.length < 3 && nameTouched) -> "El nombre debe tener al menos 3 caracteres"
            (name.length > 35 && nameTouched) -> "El nombre debe tener menos de 35 caracteres"
            else ->null
        }


    fun validateEmail(): String? =
        if (email.isBlank() && emailTouched) "El correo no puede estar vacío"
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) "Correo inválido"
        else
        when{
            (email.length > 65 && emailTouched)-> "El correo debe tener menos de 65 caracteres"
            else ->null
        }


    fun validatePass(): String? =
        when {
            (pass.length < 6 && passTouched)-> "La contraseña debe tener al menos 6 caracteres"
            (pass.length > 30 && passTouched) -> "La contraseña debe tener menos de 30 caracteres"
            else -> null
        }

    fun validateConfirm(): String? =
        if (confirm != pass && confirmTouched) "Las contraseñas no coinciden" else null


    // validar todo y setear errores
    fun validateAll(): Boolean {
        val nErr = validateName()
        val eErr = validateEmail()
        val pErr = validatePass()
        val cErr = validateConfirm()

        nameError = nErr
        emailError = eErr
        passError = pErr
        confirmError = cErr

        return listOf(nErr, eErr, pErr, cErr).all { it == null }
    }

    // Lienzo base
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.BlueInterface)), // azul de fondo
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Marca superior
        Text(
            text = "Sombri-Ya",
            color = Color(0xFF001242),
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(top = 50.dp)
        )

        // -------- Tarjeta centrada vertical/horizontal --------
        Box(
            modifier = Modifier
                .fillMaxSize()
            ,
            contentAlignment = Alignment.Center,

        ) {
            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .wrapContentHeight()
                    .shadow(8.dp, RoundedCornerShape(50.dp), clip = false)
                ,
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

                    // Icono en cajita
                    Box(
                        modifier = Modifier
                            .size(width = 53.dp, height = 57.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "personIcon",
                            tint = Color(0xFF001242),
                            modifier = Modifier.size(50.dp)
                        )
                    }


                    // Campos
                    LabeledInput(
                        label = "Nombre",
                        value = name,
                        onValueChange = { name = it
                            if (nameTouched || submitAttempted) nameError = validateName()
                        },
                        onFocusChanged = { focused ->
                            if (focused) {
                                nameTouched = true
                                nameError = validateName()
                            }
                        },
                        hint = "Tu nombre",
                        modifier = Modifier.fillMaxWidth(),
                        errorMessage = if (nameTouched || submitAttempted) nameError else null
                    )
                    LabeledInput(
                        label = "Correo electrónico",
                        value = email,
                        onValueChange = {
                            email = it
                            if (emailTouched || submitAttempted) emailError = validateEmail()
                        },
                        onFocusChanged = { focused ->
                            if (focused) {
                                emailTouched = true
                                emailError = validateEmail()
                            }
                        },
                        hint = "usuario@example.com",
                        modifier = Modifier.fillMaxWidth(),
                        errorMessage = if (emailTouched || submitAttempted) emailError else null
                    )
                    LabeledInput(
                        label = "Contraseña",
                        value = pass,
                        onValueChange = {
                            pass = it
                            if (passTouched || submitAttempted) passError = validatePass()
                            // si confirm ya tiene texto, revalidar confirm también (coincidencia)
                            if (confirm.isNotEmpty() && (confirmTouched || submitAttempted)) {
                                confirmError = validateConfirm()
                            }
                        },
                        onFocusChanged = { focused ->
                            if (focused) {
                                passTouched = true
                                passError = validatePass()
                            }
                        },
                        hint = "mínimo 6 caracteres",
                        modifier = Modifier.fillMaxWidth(),
                        isPassword = true,
                        errorMessage = if (passTouched || submitAttempted) passError else null
                    )
                    LabeledInput(
                        label = "Confirmar contraseña",
                        value = confirm,
                        onValueChange = {
                            confirm = it
                            if (confirmTouched || submitAttempted) confirmError = validateConfirm()
                        },
                        onFocusChanged = { focused ->
                            if (focused) {
                                confirmTouched = true
                                confirmError = validateConfirm()
                            }
                        },
                        hint = "Reingresa la contraseña",
                        modifier = Modifier.fillMaxWidth(),
                        isPassword = true,
                        errorMessage = if (confirmTouched || submitAttempted) confirmError else null
                    )
                    // -------- Botón rojo anclado abajo --------
                    Button(
                        onClick = {
                            submitAttempted = true
                            if (validateAll()) {
                                // todo OK -> llamar al viewModel
                                viewModel.registerUser(name, email, pass)
                            } else {
                                // se mostrarán errores por submitAttempted = true
                            }                 },
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
                            Text("Registrar", fontSize = 18.sp)
                        }
                    }
                }


            }// column
        }
        SomenthingWentWrongPopUp(
            show = showErrorDialog,
            message = errorMessage,
            onDismiss = { showErrorDialog = false }
        )
    }// column lienzo base
}

/** Etiqueta + caja gris con borde como en el login (responsive). */
@Composable
private fun LabeledInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    onFocusChanged: (Boolean) -> Unit = {},
    errorMessage: String? = null

) {
    var localFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(top = 10.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = label,
            color = if (localFocused) Color(0xFF2196F3) else Color.Gray,
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
                .onFocusChanged { focusState ->
                    localFocused = focusState.isFocused
                }
                .border(
                    width = 1.dp,
                    color = when {
                        errorMessage != null -> Color(0xFFFF4645) // rojo error
                        localFocused -> Color(0xFF2196F3) // azul foco
                        else -> Color.Gray
                    }
                )
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
                    passwordVisualTransformationDot()
                else
                    VisualTransformation.None,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                    val focused = focusState.isFocused
                    localFocused = focused
                    onFocusChanged(focused)
                }
            )
            // hint si está vacío (se dibuja sobre el BasicTextField cuando value.isEmpty())
            if (value.isEmpty()) {
                Text(
                    text = hint,
                    color = Color(0xFF1E1E1E).copy(alpha = 0.45f),
                    fontSize = 16.sp
                )
            }
        }
        // mensaje de error (si aplica)
        if (!errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                color = Color(0xFFFF4645),
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp, top = 6.dp)
            )
        }
    }
}

/** VisualTransformation básica con puntos para contraseñas (sin lógica). */
@Composable
private fun passwordVisualTransformationDot() =
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
