package com.example.sombriyakotlin.feature.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.material3.CardDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Preview()
@Composable
fun CardProfile() {
    /*
    Scaffold(
        topBar = { TopBar() }
        //,content = {ContentCard()}
    ) {
        innerPadding ->                          // ← recibe el padding
        ContentCard(
            modifier = Modifier
                .padding(innerPadding)   // <-- evita que se pegue al TopBar
                .padding(16.dp)
        )
    }*/
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
    Card(
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomEnd = 0.dp,
            bottomStart = 0.dp),  // esquinas redondeadas
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TopAppBar(
            title = { Text("Cuenta") },
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
}
//@Preview()
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentCard(modifier: Modifier = Modifier) {
    Card(modifier = modifier.size(width = 180.dp, height = 782.dp)
    ,
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.primary))
        ,
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomEnd = 16.dp,
            bottomStart = 16.dp)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally // Alinea los elementos al centro horizontalmente
        ) {

            IconButton(
                onClick = { /**/} ,
                modifier = Modifier.size(120.dp) // tamaño grande del círculo
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Foto de perfil",
                    tint = MaterialTheme.colorScheme.onPrimary, // color del ícono
                    modifier = Modifier.size(120.dp)
                )
            }
            Text("Cambiar foto de perfil")

            OutlinedTextField(
                value = "NombreEjemplo",
                onValueChange = {},
                label = { Text("Nombre") },
                placeholder = { Text("") },
                readOnly = true,
                trailingIcon  = {
                    IconButton(onClick = {/*Accion que sucede al oprimir la flecha*/})
                    {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Modificar informacion",
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    disabledContainerColor  = colorResource(R.color.secondary),
                    focusedContainerColor = colorResource(R.color.secondary),
                    unfocusedContainerColor = colorResource(R.color.secondary)),
            modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = "password",
                onValueChange = {},
                label = { Text("Contraseña") },
                readOnly = true,
                visualTransformation = PasswordVisualTransformation(),
                trailingIcon  = {
                    IconButton(onClick = {/*Accion que sucede al oprimir la flecha*/})
                    {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Modificar informacion",
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    disabledContainerColor  = colorResource(R.color.secondary),
                    focusedContainerColor = colorResource(R.color.secondary),
                    unfocusedContainerColor = colorResource(R.color.secondary)),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = "user@uniandes.edu.co",
                onValueChange = {},
                label = { Text("email") },
                readOnly = true,
                trailingIcon  = {
                    IconButton(onClick = {/*Accion que sucede al oprimir la flecha*/})
                    {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Modificar informacion",
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    disabledContainerColor  = colorResource(R.color.secondary),
                    focusedContainerColor = colorResource(R.color.secondary),
                    unfocusedContainerColor = colorResource(R.color.secondary)),
                modifier = Modifier.fillMaxWidth()
            )

            Button(onClick = {/*Accion que sucede al oprimir la flecha*/},
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.red ),
                    contentColor = Color.White
                )
                ){
                Text("Borrar cuenta")
            }
            Button(onClick = {/*Accion que sucede al oprimir la flecha*/},
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.gray ),
                    contentColor = Color.White
                )
            ){
                Text("Desactivar cuenta")
            }





        }
    }
}


