package com.example.sombriyakotlin.ui.inferiorbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.navigation.NavController
import com.example.sombriyakotlin.R
import com.example.sombriyakotlin.ui.rent.RentViewModel

@Composable
fun Bar(
    navController: NavController,
    onMenuClick: () -> Unit
) {
    val rentViewModel: RentViewModel = hiltViewModel()
    val hasActive by rentViewModel.hasActive.collectAsStateWithLifecycle()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.BlueInterface)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Botón mapa
        Button(
            onClick = { navController.navigate("main") },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(0.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.mapa),
                contentDescription = "Inicio",
                modifier = Modifier.size(35.dp)
            )
        }

        // Botón central (rentar o devolver)
        Box(
            modifier = Modifier
                .offset(y = (-30).dp)
                .size(72.dp)
                .clip(CircleShape)
        ) {
            val containerColor =
                if (hasActive) Color(0xFFBDBDBD) // Gris cuando hay alquiler activo
                else Color.Transparent

            Button(
                onClick = { navController.navigate("rent") },
                modifier = Modifier.fillMaxSize(),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = containerColor),
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.home_button),
                    contentDescription = if (hasActive) "Devolver sombrilla" else "Rentar sombrilla",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Botón menú
        Button(
            onClick = { onMenuClick() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(0.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.menu),
                contentDescription = "Menú",
                modifier = Modifier.size(45.dp)
            )
        }
    }
}
