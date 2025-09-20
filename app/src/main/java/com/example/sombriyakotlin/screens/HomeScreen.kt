package com.example.sombriyakotlin.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Pantalla Home - Para uso futuro
 * Esta pantalla está lista para ser implementada cuando sea necesario
 */
@Composable
fun HomeScreen(onLogout: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "¡Bienvenido a Sombri-Ya!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF001242)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Esta es la pantalla principal de la aplicación.",
            fontSize = 16.sp,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onLogout,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF4645),
                contentColor = Color.White
            )
        ) {
            Text("Cerrar Sesión")
        }
    }
}