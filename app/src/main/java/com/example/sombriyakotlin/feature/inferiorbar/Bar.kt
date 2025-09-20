package com.example.sombriyakotlin.feature.inferiorbar

import android.service.autofill.OnClickAction
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sombriyakotlin.R

@OptIn(ExperimentalMaterial3Api::class)
//@Preview()
@Composable
fun Bar(navController: NavController){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.BlueInterface)) // Color morado oscuro para la barra
            .padding(0.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ){

        Button(onClick = { navController.navigate("main") },
//            modifier = Modifier.size(48.dp), // Reduce el tamaño del botón
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent), // Color del botón
            contentPadding = PaddingValues(0.dp)
        ) {
            Image(painter = painterResource(id = R.drawable.mapa),
                //modifier = Modifier.fillMaxSize(), // Ajusta el tamaño de la imagen para que llene el botón
                contentDescription = "Inicio",
                modifier = Modifier.size(35.dp, 35.dp), // Ajusta el tamaño de la imagen para que llene el botón
                //tint = Color.Unspecified, // Asegura que el icono use sus propios colores
            )
        }
        // Botón central que sobresale
        Box(
            modifier = Modifier
                .offset(y = (-30).dp) // Desplaza el botón hacia arriba
                .size(72.dp) // Tamaño mayor para el botón central
                .clip(CircleShape) // Forma circular
                //.background(Color.Red) // Color de fondo del botón central
        ) {
            Button(
                onClick = {
                    navController.navigate("rentar")
                },
                modifier = Modifier.fillMaxSize(), // El botón ocupa todo el Box, Button
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent), // Color del botón
                contentPadding = PaddingValues(0.dp) // Sin padding interno
                ) {
                Image(painter = painterResource(id = R.drawable.home_button),
                    modifier = Modifier.fillMaxSize(), // Ajusta el tamaño de la imagen para que llene el botón
                    contentDescription = "Rentar",
                    contentScale = ContentScale.FillBounds
                    //tint = Color.Unspecified, // Asegura que el icono use sus propios colores
                    )

            }
        }
        Button(onClick = { /*TODO*/ },
//            modifier = Modifier.size(48.dp), // Reduce el tamaño del botón
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent), // Color del botón
            contentPadding = PaddingValues(0.dp)
        ) {
            Image(painter = painterResource(id = R.drawable.menu),
                modifier = Modifier.size(45.dp, 45.dp), // Ajusta el tamaño de la imagen para que llene el botón
                contentDescription = "Home",
                //contentScale = ContentScale.Fit
                //tint = Color.Unspecified, // Asegura que el icono use sus propios colores
            )
        }


    }
}