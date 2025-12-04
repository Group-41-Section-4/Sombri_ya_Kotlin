package com.example.sombriyakotlin.ui.tyc

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.sombriyakotlin.ui.layout.TopBarMini

@Composable
fun TyCScreen(navController: NavHostController) {

    Scaffold(
        topBar = { TopBarMini(navController, "Términos y condiciones") }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {

            item {
                Text(
                    text = "Términos y condiciones de uso",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Última actualización: noviembre de 2025",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(Modifier.height(16.dp))
            }

            item {
                TextBlock(
                    title = "1. Responsable del tratamiento de datos",
                    content = "La aplicación es operada por el equipo desarrollador de Sombri-Ya, quien actúa como responsable del tratamiento de los datos personales que se recolectan a través de la misma."
                )
            }

            item {
                TextBlock(
                    title = "2. Datos que recopilamos",
                    content = """
                        Al utilizar la aplicación, podemos recopilar los siguientes tipos de datos:

                        • Datos de identificación: nombre, correo electrónico y otra información básica necesaria para crear y mantener tu cuenta.

                        • Datos de uso de la aplicación: historial de alquileres, estaciones utilizadas, tiempos de uso, calificaciones y reportes de sombrillas.

                        • Datos de ubicación aproximada: cuando sea necesario para mostrar estaciones cercanas o mejorar la experiencia (siempre con permisos).

                        • Datos técnicos: información del dispositivo, versión de la app y registros técnicos para diagnóstico.
                    """.trimIndent()
                )
            }

            item {
                TextBlock(
                    title = "3. Finalidades del tratamiento de datos",
                    content = """
                        Utilizamos tus datos personales para:

                        • Gestionar el registro y autenticación de tu cuenta.
                        • Permitir y registrar alquileres de sombrillas.
                        • Procesar reportes, calificaciones y comentarios.
                        • Mejorar el funcionamiento y analizar patrones de uso.
                        • Cumplir obligaciones legales y atender requerimientos.
                    """.trimIndent()
                )
            }

            item {
                TextBlock(
                    title = "4. Almacenamiento y conservación",
                    content = """
                        Tus datos pueden almacenarse localmente o en servidores remotos. 
                        Se conservarán mientras mantengas activa tu cuenta o mientras sean necesarios.
                    """.trimIndent()
                )
            }

            item {
                TextBlock(
                    title = "5. Seguridad de la información",
                    content = """
                        Implementamos medidas razonables de seguridad como cifrado en tránsito y controles de acceso. 
                        Sin embargo, ningún sistema es completamente invulnerable.
                    """.trimIndent()
                )
            }

            item {
                TextBlock(
                    title = "6. Compartir datos con terceros",
                    content = """
                        No vendemos datos personales.

                        Se podrán compartir únicamente para:
                        • Servicios tecnológicos (infraestructura, análisis) bajo confidencialidad.
                        • Cumplir requerimientos legales o judiciales.
                    """.trimIndent()
                )
            }

            item {
                TextBlock(
                    title = "7. Derechos del usuario",
                    content = """
                        Puedes ejercer los siguientes derechos:

                        • Acceder a tu información.
                        • Solicitar actualización o rectificación.
                        • Solicitar supresión cuando proceda.
                        • Retirar tu consentimiento cuando aplique.
                    """.trimIndent()
                )
            }

            item {
                TextBlock(
                    title = "8. Uso en modo sin conexión",
                    content = """
                        La aplicación puede almacenar temporalmente datos en tu dispositivo para uso offline. 
                        Estos se sincronizan al recuperar conexión y se eliminan cuando ya no son necesarios.
                    """.trimIndent()
                )
            }

            item {
                TextBlock(
                    title = "9. Modificaciones a los términos",
                    content = """
                        Podremos actualizar estos términos para reflejar cambios en la app, en el tratamiento de datos o en la normativa. 
                        Se te notificará cuando haya cambios relevantes.
                    """.trimIndent()
                )
            }

            item {
                TextBlock(
                    title = "10. Contacto",
                    content = """
                        Si tienes dudas o comentarios sobre estos términos o sobre el tratamiento de tus datos, 
                        puedes contactarnos a través de los canales de soporte dentro de la aplicación.
                    """.trimIndent()
                )
            }

            item { Spacer(Modifier.height(40.dp)) }
        }
    }
}

@Composable
fun TextBlock(title: String, content: String) {
    Column  {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 20.sp
        )
        Spacer(Modifier.height(16.dp))
    }
}
