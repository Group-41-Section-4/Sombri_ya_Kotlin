package com.example.sombriyakotlin.ui.help

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Message
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.sombriyakotlin.ui.layout.TopBarMini

@Composable
fun HelpScreen(navController: NavHostController) {
    val cardBackground = Color(0xFFF3F4F6)
    val subtleText = Color(0xFF7A7A7A)

    Scaffold(
        topBar = {
            TopBarMini(navController,"Ayuda")
        },
        containerColor = Color.White
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            item {
                // Preguntas frecuentes (Card grande)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = cardBackground),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Preguntas frecuentes",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            fontSize = 18.sp
                        )
                        Spacer(Modifier.height(12.dp))

                        FAQItem(
                            question = "¿Qué métodos de pago tienen disponibles?",
                            answer = "Recomendamos tarjetas y Nequi.",
                            answerColor = subtleText
                        )

                        Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE3DDD9))

                        FAQItem(
                            question = "¿Qué hago si olvidé mi contraseña?",
                            answer = "Puedes recuperar tu cuenta desde la opción \"Perfil\".",
                            answerColor = subtleText
                        )
                    }
                }
            }

            item {
                // Tutoriales (card)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = cardBackground),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Tutoriales",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            fontSize = 18.sp
                        )
                        Spacer(Modifier.height(12.dp))

                        val tutorials = listOf(
                            "Reservar una sombrilla",
                            "Agregar métodos de pago",
                            "Reportar una sombrilla dañada"
                        )

                        tutorials.forEach { title ->
                            TutorialItem(title = title, onClick = { /* navegar o reproducir video */ })
                        }
                    }
                }
            }

            item {
                // Contacto (card)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = cardBackground),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Contáctanos",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            fontSize = 18.sp
                        )
                        Spacer(Modifier.height(12.dp))

                        ContactRow(
                            icon = { Icon(Icons.Default.Email, contentDescription = "email") },
                            primary = "ayuda@sombriya.com",
                            secondary = ""
                        )

                        Spacer(Modifier.height(10.dp))

                        ContactRow(
                            icon = { Icon(Icons.Default.Message, contentDescription = "chat") },
                            primary = "@Sombri-YA",
                            secondary = ""
                        )
                    }
                }
            }

            item {
                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun FAQItem(question: String, answer: String, answerColor: Color) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = question,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = answer,
            fontSize = 14.sp,
            color = answerColor
        )
    }
}

@Composable
fun TutorialItem(title: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.PlayCircleOutline,
            contentDescription = "play",
            modifier = Modifier.size(22.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(text = title, fontSize = 15.sp)
    }
}

@Composable
fun ContactRow(icon: @Composable () -> Unit, primary: String, secondary: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF0ECE8)),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(text = primary, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            if (secondary.isNotEmpty()) {
                Text(text = secondary, fontSize = 13.sp, color = Color.Gray)
            }
        }
    }
}
