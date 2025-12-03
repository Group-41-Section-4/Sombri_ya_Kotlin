package com.example.sombriyakotlin.ui.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.sombriyakotlin.R
import com.example.sombriyakotlin.ui.navigation.Routes
import com.example.sombriyakotlin.ui.navigation.safeNavigate
import com.example.sombriyakotlin.ui.rent.RentViewModel
import androidx.compose.runtime.getValue

@Composable
fun Bar(
    navController: NavHostController
) {
    val rentViewModel: RentViewModel = hiltViewModel()
    val hasActive by rentViewModel.hasActive.collectAsStateWithLifecycle()

    Row(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .background(colorResource(id = R.color.BlueInterface)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Companion.CenterVertically
    ) {
        // Botón mapa
        Button(
            onClick = {
                navController.safeNavigate(Routes.MAIN, Routes.MAIN)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Companion.Transparent),
            contentPadding = PaddingValues(0.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.size(40.dp)) {
                Icon(Icons.Default.Map, contentDescription = "Mapa",tint = Color.Black, modifier = Modifier.size(24.dp))
                Text("Mapa", style = MaterialTheme.typography.labelSmall, color = Color.Black)
            }
        }

        // Botón central (rentar o devolver)
        Box(
            modifier = Modifier.Companion
                .offset(y = (-30).dp)
                .size(72.dp)
                .clip(CircleShape)
        ) {
            val containerColor =
                if (hasActive) Color(0xFFBDBDBD) // Gris cuando hay alquiler activo
                else Color.Companion.Transparent

            Button(
                onClick = {
                    navController.safeNavigate(Routes.RENT, Routes.MAIN)
                },
                modifier = Modifier.Companion.fillMaxSize(),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = containerColor),
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.home_button),
                    contentDescription = if (hasActive) "Devolver sombrilla" else "Rentar sombrilla",
                    contentScale = ContentScale.Companion.FillBounds,
                    modifier = Modifier.Companion.fillMaxSize()
                )
            }
        }

        // Botón menú
        Button(
            onClick = {
                navController.safeNavigate(Routes.MENU, Routes.MAIN)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Companion.Transparent),
            contentPadding = PaddingValues(0.dp)
        ) {

            // Ítem "Más" (seleccionado)
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.size(40.dp)) {
                Icon(Icons.Default.Menu, contentDescription = "Más", tint = Color.Black, modifier = Modifier.size(24.dp))
                Text("Más", style = MaterialTheme.typography.labelSmall, color = Color.Black)
            }
        }
    }
}