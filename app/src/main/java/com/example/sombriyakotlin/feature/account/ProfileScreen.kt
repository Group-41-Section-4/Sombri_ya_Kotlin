package com.example.sombriyakotlin.feature.account

import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Preview()
@Composable
fun CardProfile() {

    Column {
        TopBar()
        ContentCard(
            modifier = Modifier.fillMaxWidth()
        )
    }
    }
@OptIn(ExperimentalMaterial3Api::class)
//@Preview
@Composable()
fun TopBar(){

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
                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Atrás"
                    )
                }
            }
        )
    }

//@Preview()
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentCard(modifier: Modifier = Modifier) {

        Column(
            modifier = Modifier.fillMaxSize().background(Color.White),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            IconButton(
                onClick = { /**/} ,
                modifier = Modifier.size(160.dp) // tamaño grande del círculo
            ) {
                Icon(

                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Foto de perfil",
                    tint = Color.Black, // color del ícono
                    modifier = Modifier.size(166.dp)
                )
            }

            Text("Cambiar foto de perfil")

            OutlinedTextField(
                value = "NombreEjemplo",
                onValueChange = {},
                readOnly = true,
                shape = RoundedCornerShape(24.dp),
                prefix = {
                    Text("Nombre", fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 160.dp))
                },
                trailingIcon  = {
                    IconButton(onClick = {/*Accion que sucede al oprimir la flecha*/})
                    {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow),
                            contentDescription = "Modificar informacion",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    disabledContainerColor  = colorResource(R.color.secondary),
                    focusedContainerColor = colorResource(R.color.secondary),
                    unfocusedContainerColor = colorResource(R.color.secondary),
                    focusedTextColor   = colorResource(R.color.gray),
                    unfocusedTextColor = colorResource(R.color.gray),

                    focusedIndicatorColor= colorResource(R.color.primary),
                    unfocusedIndicatorColor= colorResource(R.color.primary)
                ),
                        modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = "password",
                onValueChange = {},

                readOnly = true,
                shape = RoundedCornerShape(24.dp),
                prefix = {
                    Text("Contraseña", fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 190.dp))

                },
                visualTransformation = PasswordVisualTransformation(),
                trailingIcon  = {
                    IconButton(onClick = {/*Accion que sucede al oprimir la flecha*/})
                    {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow),
                            contentDescription = "Modificar informacion",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    disabledContainerColor  = colorResource(R.color.secondary),
                    focusedContainerColor = colorResource(R.color.secondary),
                    unfocusedContainerColor = colorResource(R.color.secondary),
                    focusedTextColor   = colorResource(R.color.gray),
                    unfocusedTextColor = colorResource(R.color.gray),
                    focusedIndicatorColor= colorResource(R.color.primary),
                    unfocusedIndicatorColor= colorResource(R.color.primary)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = "user@uniandes.edu.co",
                onValueChange = {},

                readOnly = true,
                shape = RoundedCornerShape(24.dp),
                prefix = {
                    Text("Email", fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 130.dp))

                },
                trailingIcon  = {
                    IconButton(onClick = {/*Accion que sucede al oprimir la flecha*/})
                    {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow),
                            contentDescription = "Modificar informacion",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    disabledContainerColor  = colorResource(R.color.secondary),
                    focusedContainerColor = colorResource(R.color.secondary),
                    unfocusedContainerColor = colorResource(R.color.secondary),
                    focusedTextColor   = colorResource(R.color.gray),
                    unfocusedTextColor = colorResource(R.color.gray),
                    focusedIndicatorColor= colorResource(R.color.primary),
                    unfocusedIndicatorColor= colorResource(R.color.primary)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Button(onClick = {/*Accion que sucede al oprimir la flecha*/},
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.red ),
                    contentColor = Color.White,
                ),
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(48.dp)
                ){
                Text("Borrar cuenta")
            }
            Button(onClick = {/*Accion que sucede al oprimir la flecha*/},
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.gray ),
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(48.dp)
            ){
                Text("Desactivar cuenta")
            }





        }
    }
//}


